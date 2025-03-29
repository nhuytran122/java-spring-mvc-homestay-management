package com.lullabyhomestay.homestay_management.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.config.VNPayConfig;
import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.Payment;
import com.lullabyhomestay.homestay_management.domain.dto.SearchPaymentCriteriaDTO;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.PaymentRepository;
import com.lullabyhomestay.homestay_management.service.specifications.PaymentSpecification;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;
import com.lullabyhomestay.homestay_management.utils.Constants;
import com.lullabyhomestay.homestay_management.utils.PaymentPurpose;
import com.lullabyhomestay.homestay_management.utils.PaymentStatus;
import com.lullabyhomestay.homestay_management.utils.PaymentType;
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

    public String createVnPayPaymentURL(HttpServletRequest request, Long bookingID, PaymentPurpose paymentPurpose) {
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

        // Thanh toán -> chuyển trạng thái cho Booking từ PENDING -> CONFIRMED
        currentBooking.setStatus(BookingStatus.CONFIRMED);
        bookingService.handleSaveBooking(currentBooking);

        Double oldPaidAmount = currentBooking.getPaidAmount() != null ? currentBooking.getPaidAmount() : 0;
        currentBooking.setPaidAmount(oldPaidAmount + payment.getTotalAmount());
        paymentDetailService.handleSavePaymentDetail(newPayment, paymentPurpose);
        return newPayment;
    }

    public Page<Payment> searchPayments(SearchPaymentCriteriaDTO criteria, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE, Sort.by("paymentDate").descending());

        boolean isAllCriteriaEmpty = (criteria.getKeyword() == null || criteria.getKeyword().isEmpty())
                && (criteria.getTimeRange() == null || criteria.getTimeRange().isEmpty())
                && (criteria.getStatus() == null || criteria.getStatus().isEmpty())
                && (criteria.getType() == null || criteria.getType().isEmpty());

        if (isAllCriteriaEmpty) {
            return paymentRepository.findAll(pageable);
        }

        PaymentStatus status = null;
        if (criteria.getStatus() != null && !criteria.getStatus().isEmpty()) {
            try {
                status = PaymentStatus.valueOf(criteria.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                status = null;
            }
        }

        PaymentType type = null;
        if (criteria.getType() != null && !criteria.getType().isEmpty()) {
            try {
                type = PaymentType.valueOf(criteria.getType().toUpperCase());
            } catch (IllegalArgumentException e) {
                type = null;
            }
        }

        Specification<Payment> spec = Specification.where(PaymentSpecification.statusEqual(status))
                .and(PaymentSpecification.typeEqual(type))
                .and(PaymentSpecification.paymentDateBetween(criteria.getFromTime(), criteria.getToTime()))
                .and(PaymentSpecification.customerNameLike(criteria.getKeyword())
                        .or(PaymentSpecification.customerPhoneLike(criteria.getKeyword()))
                        .or(PaymentSpecification.customerEmailLike(criteria.getKeyword())));

        return paymentRepository.findAll(spec, pageable);
    }

    public Payment getPaymentByID(Long paymentID) {
        Optional<Payment> paymentOpt = paymentRepository.findByPaymentID(paymentID);
        if (!paymentOpt.isPresent()) {
            throw new NotFoundException("Lịch sử thanh toán");
        }
        return paymentOpt.get();
    }
}