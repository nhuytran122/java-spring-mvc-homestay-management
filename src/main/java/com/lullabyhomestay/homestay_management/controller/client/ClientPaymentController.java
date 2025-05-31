package com.lullabyhomestay.homestay_management.controller.client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.Payment;
import com.lullabyhomestay.homestay_management.domain.dto.ApiResponseDTO;
import com.lullabyhomestay.homestay_management.service.BookingExtensionService;
import com.lullabyhomestay.homestay_management.service.BookingService;
import com.lullabyhomestay.homestay_management.service.PaymentService;
import com.lullabyhomestay.homestay_management.utils.PaymentPurpose;
import com.lullabyhomestay.homestay_management.utils.PaymentStatus;
import com.lullabyhomestay.homestay_management.utils.PaymentType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@PreAuthorize("hasRole('CUSTOMER')")
public class ClientPaymentController {

    private final PaymentService paymentService;
    private final BookingService bookingService;
    private final BookingExtensionService bookingExtensionService;

    @GetMapping("/checkout")
    @ResponseBody
    public ResponseEntity<ApiResponseDTO<String>> handlePay(@RequestParam Long bookingId,
            @RequestParam PaymentPurpose paymentPurpose, HttpServletRequest request, Model model) {
        String paymentUrl = paymentService.createVnPayPaymentURL(request, bookingId, paymentPurpose);
        return ResponseEntity.ok(new ApiResponseDTO<>(paymentUrl, "Thực hiện thanh toán"));
    }

    @GetMapping("/checkout/vn-pay-callback")
    public String payCallbackHandler(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        String status = request.getParameter("vnp_ResponseCode");
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String[] parts = orderInfo.split("_PURPOSE_");
        Long bookingId = Long.parseLong(parts[0].replace("BOOKING_", ""));
        PaymentPurpose paymentPurpose = PaymentPurpose.valueOf(parts[1]);
        if (status.equals("00")) {
            Payment payment = new Payment();
            payment.setPaymentType(PaymentType.TRANSFER);
            payment.setStatus(PaymentStatus.COMPLETED);

            String vnpTxnRef = request.getParameter("vnp_TxnRef");
            String vnpPayDate = request.getParameter("vnp_PayDate");
            String vnpTransactionNo = request.getParameter("vnp_TransactionNo");

            payment.setVnpTxnRef(vnpTxnRef);
            LocalDateTime paymentDate = null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            paymentDate = LocalDateTime.parse(vnpPayDate, formatter);
            payment.setPaymentDate(paymentDate);
            payment.setVnpTransactionNo(vnpTransactionNo);
            if (paymentPurpose == PaymentPurpose.ROOM_BOOKING) {
                HttpSession session = request.getSession(false);
                session.setAttribute("bookingRequest", null);
            }

            Booking booking = bookingService.getBookingById(bookingId);
            payment.setBooking(booking);
            String vnpAmountStr = request.getParameter("vnp_Amount");
            if (vnpAmountStr == null) {
                throw new IllegalArgumentException("vnp_Amount không tồn tại");
            }
            Double totalAmount = Double.parseDouble(vnpAmountStr) / 100;
            payment.setTotalAmount(totalAmount);
            paymentService.handleSavePaymentWhenCheckout(payment, paymentPurpose);

            return "redirect:/booking/booking-history/" + bookingId;
        } else if (status.equals("24")) {
            if (paymentPurpose == PaymentPurpose.EXTENDED_HOURS) {
                bookingExtensionService.deleteLatestExtensionByBookingId(bookingId);
                redirectAttributes.addFlashAttribute("errorMessage", "Giao dịch của bạn đã bị hủy");
            }
        }

        return "redirect:/checkout/payment-failed";

    }

    @GetMapping("/checkout/payment-failed")
    public String getPaymentFailedPage() {
        return "client/checkout/payment-failed";
    }

}
