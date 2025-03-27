package com.lullabyhomestay.homestay_management.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.config.VNPayConfig;
import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.Payment;
import com.lullabyhomestay.homestay_management.repository.PaymentRepository;
import com.lullabyhomestay.homestay_management.utils.PaymentPurpose;
import com.lullabyhomestay.homestay_management.utils.VNPayUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentService {
    private final VNPayConfig vnPayConfig;
    private final PaymentRepository paymentRepository;
    private final BookingService bookingService;
    private final PaymentDetailService paymentDetailService;

    public String createVnPayPayment(HttpServletRequest request, Long bookingID, PaymentPurpose paymentPurpose) {
        Booking booking = bookingService.getBookingByID(bookingID);
        Double totalAmountDouble = booking.getTotalAmount();
        Long amount = totalAmountDouble.longValue() * 100L;

        String bankCode = request.getParameter("bankCode");
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig(bookingID, paymentPurpose);
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return paymentUrl;
    }

    @Transactional
    public Payment handleSavePayment(Payment payment, PaymentPurpose paymentPurpose) {
        Payment newPayment = paymentRepository.save(payment);
        Booking currentBooking = payment.getBooking();
        Double oldPaidAmount = currentBooking.getPaidAmount() != null ? currentBooking.getPaidAmount() : 0;
        currentBooking.setPaidAmount(oldPaidAmount + payment.getTotalAmount());
        paymentDetailService.handleSavePaymentDetail(newPayment, paymentPurpose);
        return newPayment;
    }
}