package com.lullabyhomestay.homestay_management.service;

import java.time.LocalDateTime;
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
import com.lullabyhomestay.homestay_management.domain.BookingExtension;
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
    private final BookingExtensionService bookingExtensionService;
    private final RoomStatusHistoryService roomStatusHistoryService;
    private final BookingExtraService bookingExtraService;

    public String createVnPayPaymentURL(HttpServletRequest request, Long bookingID, PaymentPurpose paymentPurpose) {
        Booking booking = bookingService.getBookingByID(bookingID);
        Long amount = 0L;
        if (paymentPurpose == PaymentPurpose.ROOM_BOOKING) {
            Double totalAmountDouble = booking.getTotalAmount();
            amount = totalAmountDouble.longValue() * 100L;
        } else if (paymentPurpose == PaymentPurpose.EXTENDED_HOURS) {
            // Lấy gia hạn thuê mới nhất
            BookingExtension bookingExtension = bookingExtensionService
                    .getLatestBookingExtensionByBookingID(bookingID);
            Double totalAmountDouble = bookingExtensionService.calculateFinalExtensionAmount(bookingExtension);
            amount = totalAmountDouble.longValue() * 100L;
        } else if (paymentPurpose == PaymentPurpose.ADDITIONAL_SERVICE) {
            // Lấy tổng tiền dịch vụ trả sau mà khách chưa thanh toán
            Double totalAmountDouble = bookingExtraService.calculateUnpaidServicesTotalAmount(bookingID);
            amount = totalAmountDouble.longValue() * 100L;
        }
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
    public Payment handleSavePaymentWhenCheckout(Payment payment, PaymentPurpose paymentPurpose) {
        Payment newPayment = paymentRepository.save(payment);
        Booking currentBooking = payment.getBooking();

        if (paymentPurpose == PaymentPurpose.ROOM_BOOKING) {
            // Thanh toán -> chuyển trạng thái cho Booking từ PENDING -> CONFIRMED
            currentBooking.setStatus(BookingStatus.CONFIRMED);
            bookingService.handleSaveBooking(currentBooking);
        }

        if (paymentPurpose == PaymentPurpose.EXTENDED_HOURS) {
            BookingExtension bookingExtension = bookingExtensionService
                    .getLatestBookingExtensionByBookingID(currentBooking.getBookingID());

            // Cập nhật checkout, giá
            bookingService.handleSaveBookingAfterExtend(currentBooking.getBookingID(), bookingExtension);

            // Cập nhật lịch trình của phòng
            roomStatusHistoryService.handleBookingExtensions(bookingExtension);
        }

        Double oldPaidAmount = currentBooking.getPaidAmount() != null ? currentBooking.getPaidAmount() : 0;
        currentBooking.setPaidAmount(oldPaidAmount + payment.getTotalAmount());
        paymentDetailService.handleSavePaymentDetail(newPayment, paymentPurpose);
        return newPayment;
    }

    @Transactional
    public Payment handleSavePaymentWithAdmin(Long bookingID, PaymentType paymentType,
            PaymentPurpose paymentPurpose) {
        Booking booking = bookingService.getBookingByID(bookingID);
        // Payment newPayment = paymentRepository.save(payment);
        Payment newPayment = new Payment();
        newPayment.setPaymentType(paymentType);
        newPayment.setStatus(PaymentStatus.COMPLETED);
        newPayment.setPaymentDate(LocalDateTime.now());
        newPayment.setBooking(booking);
        Double totalAmount = 0.0;
        if (paymentPurpose == PaymentPurpose.ROOM_BOOKING) {
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingService.handleSaveBooking(booking);
            totalAmount = booking.getTotalAmount();
        } else if (paymentPurpose == PaymentPurpose.ADDITIONAL_SERVICE) {
            totalAmount = bookingExtraService.calculateUnpaidServicesTotalAmount(bookingID);
        } else {
            BookingExtension bookingExtension = bookingExtensionService
                    .getLatestBookingExtensionByBookingID(bookingID);
            totalAmount = bookingExtensionService.calculateFinalExtensionAmount(bookingExtension);
        }
        if (paymentPurpose == PaymentPurpose.EXTENDED_HOURS) {
            BookingExtension bookingExtension = bookingExtensionService
                    .getLatestBookingExtensionByBookingID(booking.getBookingID());

            // Cập nhật checkout, giá
            bookingService.handleSaveBookingAfterExtend(booking.getBookingID(),
                    bookingExtension);

            // Cập nhật lịch trình của phòng
            roomStatusHistoryService.handleBookingExtensions(bookingExtension);
        }
        newPayment.setTotalAmount(totalAmount);

        newPayment = paymentRepository.save(newPayment);
        Double oldPaidAmount = booking.getPaidAmount() != null ? booking.getPaidAmount() : 0;
        booking.setPaidAmount(oldPaidAmount + newPayment.getTotalAmount());
        paymentDetailService.handleSavePaymentDetail(newPayment, paymentPurpose);
        return newPayment;
    }

    public Page<Payment> searchPayments(SearchPaymentCriteriaDTO criteria, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE, Sort.by("paymentDate").descending());

        boolean isAllCriteriaEmpty = (criteria.getTimeRange() == null || criteria.getTimeRange().isEmpty())
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
                .and(PaymentSpecification.paymentDateBetween(criteria.getFromTime(), criteria.getToTime()));

        return paymentRepository.findAll(spec, pageable);
    }

    public Payment getPaymentByID(Long paymentID) {
        Optional<Payment> paymentOpt = paymentRepository.findByPaymentID(paymentID);
        if (!paymentOpt.isPresent()) {
            throw new NotFoundException("Lịch sử thanh toán");
        }
        return paymentOpt.get();
    }

    public Payment getPaymentByBookingID(Long bookingID) {
        Optional<Payment> paymentOpt = paymentRepository.findByBooking_BookingID(bookingID);
        if (!paymentOpt.isPresent())
            return null;
        return paymentOpt.get();
    }
}