package com.lullabyhomestay.homestay_management.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.Customer;
import com.lullabyhomestay.homestay_management.domain.Payment;
import com.lullabyhomestay.homestay_management.domain.Refund;
import com.lullabyhomestay.homestay_management.domain.dto.SearchBookingCriteriaDTO;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.BookingRepository;
import com.lullabyhomestay.homestay_management.repository.PaymentRepository;
import com.lullabyhomestay.homestay_management.repository.RefundRepository;
import com.lullabyhomestay.homestay_management.repository.RoomStatusHistoryRepository;
import com.lullabyhomestay.homestay_management.service.specifications.BookingSpecifications;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;
import com.lullabyhomestay.homestay_management.utils.Constants;
import com.lullabyhomestay.homestay_management.utils.DiscountUtil;
import com.lullabyhomestay.homestay_management.utils.PaymentStatus;
import com.lullabyhomestay.homestay_management.utils.PaymentType;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomStatusHistoryService roomStatusHistoryService;
    private final RoomStatusHistoryRepository roomStatusHistoryRepo;
    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final RefundService refundService;

    public Page<Booking> searchBookings(SearchBookingCriteriaDTO criteria, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE,
                "asc".equals(criteria.getSort()) ? Sort.by("CheckIn").ascending()
                        : "desc".equals(criteria.getSort()) ? Sort.by("CheckIn").descending() : Sort.unsorted());
        boolean isAllCriteriaEmpty = (criteria.getKeyword() == null || criteria.getKeyword().isEmpty())
                && criteria.getBranchID() == null
                && criteria.getRoomTypeID() == null
                && (criteria.getStatus() == null || criteria.getStatus().isEmpty())
                && (criteria.getTimeRange() == null || criteria.getTimeRange().isEmpty())
                && criteria.getCustomerID() == null;

        if (isAllCriteriaEmpty) {
            return bookingRepository.findAll(pageable);
        }
        BookingStatus statusNum = null;
        if (criteria.getStatus() != null && !criteria.getStatus().isEmpty()) {
            try {
                statusNum = BookingStatus.valueOf(criteria.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                statusNum = null;
            }
        }
        Specification<Booking> spec = Specification.where(BookingSpecifications.hasBranch(criteria.getBranchID()))
                .and(BookingSpecifications.hasRoomType(criteria.getRoomTypeID()))
                .and(BookingSpecifications.customerNameLike(criteria.getKeyword())
                        .and(BookingSpecifications.statusEqual(statusNum))
                        .and(BookingSpecifications.checkInBetween(criteria.getFromTime(), criteria.getToTime())))
                .and(BookingSpecifications.hasCustomer(criteria.getCustomerID()));
        return bookingRepository.findAll(spec, pageable);
    }

    @Transactional
    public Booking handleBooking(Booking booking) {
        booking.setTotalAmount(calculateTotalAmountBookingRoom(booking, booking.getCustomer()));
        Booking savedBooking = bookingRepository.save(booking);

        // Cập nhật lịch trình phòng để giữ phòng cho khách
        roomStatusHistoryService.handleStatusWhenBooking(savedBooking);
        return savedBooking;
    }

    public Booking getBookingByID(Long bookingID) {
        Optional<Booking> bookingOpt = bookingRepository.findByBookingID(bookingID);
        if (!bookingOpt.isPresent()) {
            throw new NotFoundException("Lịch đặt phòng");
        }
        return bookingOpt.get();
    }

    public Booking handleSaveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Long countByBookingStatusAndCustomerID(BookingStatus bookingStatus, Long customerID) {
        return bookingRepository.countByStatusAndCustomer_CustomerID(bookingStatus, customerID);
    }

    public Long countTotalBookingByCustomerID(Long customerID) {
        return bookingRepository.countByCustomer_CustomerID(customerID);
    }

    public List<Booking> getListBookingsByCustomerID(Long customerID) {
        return bookingRepository.findByCustomer_CustomerID(customerID);
    }

    public Double getTotalAmountByCustomerID(Long customerID) {
        Double totalAmount = bookingRepository.getTotalAmountByCustomerId(customerID);
        return totalAmount != null ? totalAmount : 0.0;
    }

    @Transactional
    public void cancelBooking(Long bookingID) {
        // Lấy và validate booking
        Booking currentBooking = validateAndGetBooking(bookingID);
        if (currentBooking.getStatus() == BookingStatus.PENDING) {
            roomStatusHistoryRepo.deleteByBooking_BookingID(bookingID);
            // Cập nhật trạng thái đơn là CANCELLED
            updateBookingAfterCancellation(currentBooking);
            return;
        }

        if (canCancelBooking(currentBooking)) {
            // Xóa dữ liệu liên quan
            // Không xóa bookingServices để truy vết paymentDetails
            roomStatusHistoryRepo.deleteByBooking_BookingID(bookingID);
            // currentBooking.setBookingServices(new ArrayList<>());

            // Xử lý payment và refund
            Payment payment = validateAndGetPayment(currentBooking);
            Refund refund = createPendingRefund(payment, currentBooking);

            // Cập nhật booking
            updateBookingAfterCancellation(currentBooking);
        }
    }

    public boolean canCancelBooking(Booking booking) {
        LocalDateTime now = LocalDateTime.now();
        return booking.getStatus() != BookingStatus.CANCELLED
                && booking.getStatus() != BookingStatus.COMPLETED
                && now.isBefore(booking.getCheckIn());
    }

    private Booking validateAndGetBooking(Long bookingID) {
        Booking booking = getBookingByID(bookingID);
        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Chỉ được hủy khi booking chưa bắt đầu.");
        }
        return booking;
    }

    private Payment validateAndGetPayment(Booking booking) {
        List<Payment> payments = booking.getPayments();
        if (payments == null || payments.isEmpty()) {
            throw new IllegalArgumentException(
                    "Không tìm thấy lịch sử thanh toán của booking có ID: " + booking.getBookingID());
        }
        if (payments.size() != 1) {
            throw new IllegalStateException("Booking ID " + booking.getBookingID()
                    + " phải có đúng một Payment tại thời điểm hoàn tiền, nhưng tìm thấy: " + payments.size());
        }

        Payment payment = payments.get(0);
        if (payment.getPaymentType() != PaymentType.TRANSFER) {
            throw new IllegalArgumentException(
                    "Payment ID " + payment.getPaymentID() + " không phải là thanh toán VNPay");
        }
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Payment ID " + payment.getPaymentID()
                    + " chưa được thanh toán (status: " + payment.getStatus() + ")");
        }
        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            throw new IllegalStateException("Payment ID " + payment.getPaymentID()
                    + " đã được hoàn tiền trước đó (status: " + payment.getStatus() + ")");
        }
        return payment;
    }

    private Refund createPendingRefund(Payment payment, Booking booking) {
        payment.setStatus(PaymentStatus.PENDING_REFUND);
        paymentRepository.save(payment);

        Refund refund = new Refund();
        refund.setPayment(payment);
        Double refundAmount = refundService.calculateRefundAmount(booking);
        refund.setRefundAmount(refundAmount);
        refund.setRefundType(refundService.getRefundType(booking));
        refund.setStatus(PaymentStatus.PENDING_REFUND);
        refundRepository.save(refund);
        return refund;
    }

    private void updateBookingAfterCancellation(Booking booking) {
        booking.setStatus(BookingStatus.CANCELLED);
    }

    public double calculateRawTotalAmountBookingRoom(Booking booking) {
        long hours = booking.getNumberOfHours();
        double pricePerHour = booking.getRoom().getRoomType().getPricePerHour();
        double totalAmount = pricePerHour * hours;

        if (booking.getRoom().getRoomType().getName().toUpperCase().contains("DORM")) {
            totalAmount *= booking.getGuestCount();
        }
        return totalAmount;
    }

    public double calculateTotalAmountBookingRoom(Booking booking, Customer customer) {
        double rawTotal = calculateRawTotalAmountBookingRoom(booking);
        double discountAmount = DiscountUtil.calculateDiscountAmount(rawTotal, customer);
        return rawTotal - discountAmount;
    }

    public List<Booking> getListBookingByStatus(BookingStatus bookingStatus) {
        return bookingRepository.findByStatus(bookingStatus);
    }

}
