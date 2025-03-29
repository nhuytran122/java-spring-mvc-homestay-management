package com.lullabyhomestay.homestay_management.controller.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.Payment;
import com.lullabyhomestay.homestay_management.domain.dto.ApiResponseDTO;
import com.lullabyhomestay.homestay_management.service.BookingService;
import com.lullabyhomestay.homestay_management.service.PaymentService;
import com.lullabyhomestay.homestay_management.utils.PaymentPurpose;
import com.lullabyhomestay.homestay_management.utils.PaymentStatus;
import com.lullabyhomestay.homestay_management.utils.PaymentType;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class ClientPaymentController {

    private final PaymentService paymentService;
    private final BookingService bookingService;

    @GetMapping("/checkout")
    @ResponseBody
    public ResponseEntity<ApiResponseDTO<String>> handlePay(@RequestParam Long bookingID,
            @RequestParam PaymentPurpose paymentPurpose, HttpServletRequest request, Model model) {
        String paymentUrl = paymentService.createVnPayPaymentURL(request, bookingID, paymentPurpose);
        return ResponseEntity.ok(new ApiResponseDTO<>(paymentUrl, "Thực hiện thanh toán"));
    }

    @GetMapping("/checkout/vn-pay-callback")
    public String payCallbackHandler(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            Payment payment = new Payment();
            payment.setPaymentType(PaymentType.TRANSFER);
            payment.setStatus(PaymentStatus.COMPLETED);

            String orderInfo = request.getParameter("vnp_OrderInfo");
            String transactionID = request.getParameter("vnp_BankTranNo");
            payment.setExternalTransactionID(transactionID);
            String[] parts = orderInfo.split("_PURPOSE_");
            Long bookingID = Long.parseLong(parts[0].replace("BOOKING_", ""));
            PaymentPurpose paymentPurpose = PaymentPurpose.valueOf(parts[1]);

            Booking booking = bookingService.getBookingByID(bookingID);
            payment.setBooking(booking);
            String vnpAmountStr = request.getParameter("vnp_Amount");
            if (vnpAmountStr == null) {
                throw new IllegalArgumentException("vnp_Amount không tồn tại");
            }
            Double totalAmount = Double.parseDouble(vnpAmountStr) / 100;
            payment.setTotalAmount(totalAmount);
            paymentService.handleSavePayment(payment, paymentPurpose);

            return "redirect:/booking/booking-history/" + bookingID;
        } else if (status.equals("24")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Giao dịch của bạn đã bị hủy");
        }
        return "redirect:/checkout/payment-failed";

    }

    @GetMapping("/checkout/payment-failed")
    public String getPaymentFailedPage() {
        return "client/checkout/payment-failed";
    }

}
