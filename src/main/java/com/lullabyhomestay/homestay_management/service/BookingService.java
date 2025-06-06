package com.lullabyhomestay.homestay_management.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.BookingExtension;
import com.lullabyhomestay.homestay_management.domain.BookingPricingSnapshot;
import com.lullabyhomestay.homestay_management.domain.Payment;
import com.lullabyhomestay.homestay_management.domain.Refund;
import com.lullabyhomestay.homestay_management.domain.RoomPricing;
import com.lullabyhomestay.homestay_management.domain.RoomType;
import com.lullabyhomestay.homestay_management.domain.dto.BookingPriceDTO;
import com.lullabyhomestay.homestay_management.domain.dto.SearchBookingCriteriaDTO;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.BookingRepository;
import com.lullabyhomestay.homestay_management.repository.BookingServiceRepository;
import com.lullabyhomestay.homestay_management.repository.PaymentRepository;
import com.lullabyhomestay.homestay_management.repository.RefundRepository;
import com.lullabyhomestay.homestay_management.repository.RoomPricingRepository;
import com.lullabyhomestay.homestay_management.repository.RoomStatusHistoryRepository;
import com.lullabyhomestay.homestay_management.service.specifications.BookingSpecifications;
import com.lullabyhomestay.homestay_management.utils.BookingServiceStatus;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;
import com.lullabyhomestay.homestay_management.utils.Cancelability;
import com.lullabyhomestay.homestay_management.utils.Constants;
import com.lullabyhomestay.homestay_management.utils.DiscountUtil;
import com.lullabyhomestay.homestay_management.utils.PaymentStatus;
import com.lullabyhomestay.homestay_management.utils.PaymentType;
import com.lullabyhomestay.homestay_management.utils.RoomPricingType;

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
    private final BookingServiceRepository bookingServiceRepository;
    private final RoomPricingRepository roomPricingRepository;
    private final BookingExtensionService bookingExtensionService;
    private final RoomPricingService roomPricingService;

    public Page<Booking> searchBookings(SearchBookingCriteriaDTO criteria, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE);
        boolean isAllCriteriaEmpty = (criteria.getKeyword() == null || criteria.getKeyword().isEmpty())
                && criteria.getBranchId() == null
                && criteria.getRoomTypeId() == null
                && (criteria.getStatus() == null || criteria.getStatus().isEmpty())
                && (criteria.getTimeRange() == null || criteria.getTimeRange().isEmpty())
                && criteria.getCustomerId() == null;

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
        Specification<Booking> spec = Specification.where(BookingSpecifications.hasBranch(criteria.getBranchId()))
                .and(BookingSpecifications.hasRoomType(criteria.getRoomTypeId()))
                .and(BookingSpecifications.customerNameLike(criteria.getKeyword())
                        .and(BookingSpecifications.statusEqual(statusNum))
                        .and(BookingSpecifications.checkInBetween(criteria.getFromTime(), criteria.getToTime())))
                .and(BookingSpecifications.hasCustomer(criteria.getCustomerId()));
        return bookingRepository.findAll(spec, pageable);
    }

    @Transactional
    public Booking handleBooking(Booking booking) {
        booking.setTotalAmount(calculateTotalAmountBookingRoom(booking));
        booking.setUpdatedAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);

        BookingPriceDTO dto = getRoomPriceDetail(
                booking.getRoom().getRoomType().getRoomTypeId(),
                booking.getCheckIn(),
                booking.getCheckOut());
        RoomPricing roomPricing = roomPricingService.getRoomPricingById(dto.getRoomPricingId());

        BookingPricingSnapshot snapshot = savedBooking.getPricingSnapshot();
        if (snapshot == null) {
            snapshot = new BookingPricingSnapshot();
            snapshot.setBooking(savedBooking);
        }

        snapshot.setBaseDuration(roomPricing.getBaseDuration());
        snapshot.setBasePrice(roomPricing.getBasePrice());
        snapshot.setExtraHourPrice(roomPricing.getExtraHourPrice());
        snapshot.setOvernightPrice(roomPricing.getOvernightPrice());
        snapshot.setDailyPrice(roomPricing.getDailyPrice());

        savedBooking.setPricingSnapshot(snapshot);

        // Cascade sẽ lưu snapshot
        bookingRepository.save(savedBooking);

        roomStatusHistoryService.handleStatusWhenBooking(savedBooking);
        return savedBooking;
    }

    public Booking getBookingById(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findByBookingId(bookingId);
        if (!bookingOpt.isPresent()) {
            throw new NotFoundException("Lịch đặt phòng");
        }
        return bookingOpt.get();
    }

    public Booking handleSaveBooking(Booking booking) {
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public void handleSaveBookingAfterExtend(Long bookingId,
            BookingExtension bookingExtension) {
        Booking currentBooking = getBookingById(bookingId);
        // Chỉ Update time checkout mới khi đã thanh toán
        LocalDateTime oldCheckout = bookingExtension.getBooking().getCheckOut();
        Float extendedHours = bookingExtension.getExtendedHours();

        // Lấy số phút gia hạn từ extendedHours
        long extraMinutes = Math.round(extendedHours * 60);

        LocalDateTime newCheckout = oldCheckout.plusMinutes(extraMinutes);
        currentBooking.setCheckOut(newCheckout);

        Double discountedPrice = bookingExtensionService.calculateFinalExtensionAmount(bookingExtension);
        currentBooking.setTotalAmount(currentBooking.getTotalAmount() + discountedPrice);
        handleSaveBooking(currentBooking);
    }

    public Long countByBookingStatusAndCustomerId(BookingStatus bookingStatus, Long customerId) {
        return bookingRepository.countByStatusAndCustomer_CustomerId(bookingStatus, customerId);
    }

    public Long countTotalBookingByCustomerId(Long customerId) {
        return bookingRepository.countByCustomer_CustomerId(customerId);
    }

    public List<Booking> getListBookingsByCustomerId(Long customerId) {
        return bookingRepository.findByCustomer_CustomerId(customerId);
    }

    public Double getTotalAmountByCustomerId(Long customerId) {
        Double totalAmount = bookingRepository.getTotalAmountByCustomerId(customerId);
        return totalAmount != null ? totalAmount : 0.0;
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        // Lấy và validate booking
        Booking currentBooking = validateAndGetBooking(bookingId);

        if (checkCancelability(bookingId) == Cancelability.ALLOWED) {
            // Xóa dữ liệu liên quan
            // Không xóa bookingServices để truy vết paymentDetails
            roomStatusHistoryRepo.deleteByBooking_BookingId(bookingId);
            bookingServiceRepository.bulkUpdateServiceStatusByBookingId(bookingId, BookingServiceStatus.CANCELLED);

            if (currentBooking.getStatus() == BookingStatus.PENDING) {
                // Cập nhật trạng thái đơn là CANCELLED
                updateBookingAfterCancellation(currentBooking);
                return;
            }
            // Xử lý payment và refund
            Payment payment = validateAndGetPayment(currentBooking);
            Refund refund = createPendingRefund(payment, currentBooking);

            // Cập nhật booking
            updateBookingAfterCancellation(currentBooking);
        }
    }

    public Cancelability checkCancelability(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() == BookingStatus.CANCELLED)
            return Cancelability.CANCELLED;
        if (booking.getStatus() == BookingStatus.COMPLETED)
            return Cancelability.COMPLETED;
        if (LocalDateTime.now().isAfter(booking.getCheckIn()))
            return Cancelability.CHECKIN_TIME_PASSED;
        return Cancelability.ALLOWED;
    }

    public boolean canBookServiceOrBookExtension(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        return booking.getStatus() == BookingStatus.CONFIRMED;
    }

    private Booking validateAndGetBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Chỉ được hủy khi booking chưa bắt đầu.");
        }
        return booking;
    }

    private Payment validateAndGetPayment(Booking booking) {
        List<Payment> payments = booking.getPayments();
        if (payments == null || payments.isEmpty()) {
            throw new IllegalArgumentException(
                    "Không tìm thấy lịch sử thanh toán của booking có Id: " + booking.getBookingId());
        }
        if (payments.size() != 1) {
            throw new IllegalStateException("Booking Id " + booking.getBookingId()
                    + " phải có đúng một Payment tại thời điểm hoàn tiền, nhưng tìm thấy: " + payments.size());
        }

        Payment payment = payments.get(0);
        if (payment.getPaymentType() != PaymentType.TRANSFER) {
            throw new IllegalArgumentException(
                    "Payment Id " + payment.getPaymentId() + " không phải là thanh toán VNPay");
        }
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Payment Id " + payment.getPaymentId()
                    + " chưa được thanh toán (status: " + payment.getStatus() + ")");
        }
        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            throw new IllegalStateException("Payment Id " + payment.getPaymentId()
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
        booking.setUpdatedAt(LocalDateTime.now());
        booking.setStatus(BookingStatus.CANCELLED);
    }

    public Double calculateRawTotalAmountBookingRoom(Booking booking) {
        RoomType roomType = booking.getRoom().getRoomType();
        Long roomTypeId = roomType.getRoomTypeId();
        LocalDateTime checkIn = booking.getCheckIn();
        LocalDateTime checkOut = booking.getCheckOut();
        BookingPriceDTO dto = getRoomPriceDetail(roomTypeId, checkIn, checkOut);

        Double totalPrice = dto.getTotalPrice();

        if (roomType.getName().toUpperCase().contains("DORM")) {
            totalPrice *= booking.getGuestCount();
        }
        return totalPrice;
    }

    public Double calculateTotalAmountBookingRoom(Booking booking) {
        Double rawTotal = calculateRawTotalAmountBookingRoom(booking);
        Double discountAmount = DiscountUtil.calculateDiscountAmount(rawTotal, booking.getCustomer());
        return rawTotal - discountAmount;
    }

    public List<Booking> getListBookingByStatus(BookingStatus bookingStatus) {
        return bookingRepository.findByStatus(bookingStatus);
    }

    public List<Booking> findPendingBookingsBefore(int minutes) {
        return bookingRepository.findPendingBookingsBefore(minutes);
    }

    @Transactional
    public void deleteByBookingId(Long id) {
        // bookingServiceRepository.deleteByBooking_BookingId(id);
        // roomStatusHistoryRepo.deleteByBooking_BookingId(id);
        // bookingPricingSnapshotRepo.deleteByBooking_BookingId(id);
        bookingRepository.deleteByBookingId(id);
    }

    public Double calculateRoomPrice(Long roomTypeId, LocalDateTime checkIn, LocalDateTime checkOut) {
        BookingPriceDTO dto = getRoomPriceDetail(roomTypeId, checkIn, checkOut);
        return dto.getTotalPrice();
    }

    public BookingPriceDTO getRoomPriceDetail(Long roomTypeId, LocalDateTime checkIn, LocalDateTime checkOut) {
        LocalDate checkInDate = checkIn.toLocalDate();
        LocalDate checkOutDate = checkOut.toLocalDate();

        Optional<RoomPricing> matchedPricing = roomPricingRepository
                .findFirstByRoomType_RoomTypeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        roomTypeId, checkInDate, checkOutDate);

        if (!matchedPricing.isPresent()) {
            matchedPricing = roomPricingRepository.findFirstByRoomType_RoomTypeIdAndIsDefaultTrue(roomTypeId);
        }

        if (!matchedPricing.isPresent()) {
            return new BookingPriceDTO(null, 0.0, null, 0, 0, 0);
        }

        RoomPricing pricing = matchedPricing.get();

        return calculateDetail(pricing, checkIn, checkOut);
    }

    private BookingPriceDTO calculateDetail(RoomPricing pricing, LocalDateTime checkIn, LocalDateTime checkOut) {
        Long roomPricingId = pricing.getRoomPricingId();
        double totalPrice = 0;
        double totalMinutes = Duration.between(checkIn, checkOut).toMinutes(); // Sử dụng double thay vì long
        double totalDays = Math.floor(totalMinutes / (60.0 * 24));
        double totalNights = 0; // Dùng double cho tổng số đêm
        double extraHours = 0; // Dùng double cho số giờ dư

        // Thời gian qua đêm: 22h - 8h hôm sau
        LocalTime overnightStartTime = LocalTime.of(22, 0);
        LocalTime overnightEndTime = LocalTime.of(8, 0);

        // Tính phần ngày (if >= 1 ngày)
        LocalDateTime currentStart = checkIn;
        if (totalMinutes >= 1440) {
            totalPrice += pricing.getDailyPrice() * totalDays;
            currentStart = checkIn.plusDays((long) totalDays); // Chuyển tổng ngày thành long để cộng thêm vào
                                                               // currentStart
        }

        // Kiểm tra if phần còn lại chứa 1 lần qua đêm
        if (isOvernightPeriod(currentStart, checkOut, overnightStartTime, overnightEndTime)) {
            totalPrice += pricing.getOvernightPrice();
            totalNights++;

            // Trừ phần thời gian qua đêm ra khỏi khoảng time còn lại
            double overnightHours = calculateOvernightDuration(currentStart, checkOut, overnightStartTime,
                    overnightEndTime);
            double totalRemainingHours = Duration.between(currentStart, checkOut).toMinutes() / 60.0;
            double remainingHours = totalRemainingHours - overnightHours;

            if (remainingHours > 0) {
                totalPrice += calculateHourlyPrice(pricing, remainingHours, false);
                extraHours = remainingHours;
            }
        } else {
            // Không qua đêm -> chỉ tính theo giờ
            double remainingHours = Duration.between(currentStart, checkOut).toMinutes() / 60.0;
            totalPrice += calculateHourlyPrice(pricing, remainingHours, totalDays == 0); // Nếu không có ngày, đêm → có
                                                                                         // áp dụng chính sách giờ cơ
                                                                                         // bản
            extraHours = remainingHours;
        }

        // Xác định kiểu giá
        RoomPricingType pricingType;
        if (totalDays > 0 && (totalNights > 0 || extraHours > 0)) {
            pricingType = RoomPricingType.MIXED;
        } else if (totalDays > 0) {
            pricingType = RoomPricingType.DAILY;
        } else if (totalNights > 0 && extraHours > 0) {
            pricingType = RoomPricingType.MIXED;
        } else if (totalNights > 0) {
            pricingType = RoomPricingType.OVERNIGHT;
        } else {
            pricingType = RoomPricingType.HOURLY;
        }
        // double finalHours = Math.ceil(extraHours);
        return new BookingPriceDTO(roomPricingId, totalPrice, pricingType, extraHours, totalDays, totalNights);
    }

    private double calculateOvernightDuration(LocalDateTime start, LocalDateTime end,
            LocalTime overnightStartTime, LocalTime overnightEndTime) {

        LocalDate overnightDate = start.toLocalDate();
        LocalDateTime overnightStart = LocalDateTime.of(overnightDate, overnightStartTime);
        LocalDateTime overnightEnd = overnightEndTime.isBefore(overnightStartTime)
                ? LocalDateTime.of(overnightDate.plusDays(1), overnightEndTime)
                : LocalDateTime.of(overnightDate, overnightEndTime);

        LocalDateTime overlapStart = start.isAfter(overnightStart) ? start : overnightStart;
        LocalDateTime overlapEnd = end.isBefore(overnightEnd) ? end : overnightEnd;

        Duration overlap = Duration.between(overlapStart, overlapEnd);
        return !overlap.isNegative() ? overlap.toMinutes() / 60.0 : 0.0;
    }

    private boolean isOvernightPeriod(LocalDateTime start, LocalDateTime end,
            LocalTime overnightStartTime, LocalTime overnightEndTime) {

        LocalDate overnightDate = start.toLocalDate();
        LocalDateTime overnightStart = LocalDateTime.of(overnightDate, overnightStartTime);
        LocalDateTime overnightEnd = overnightEndTime.isBefore(overnightStartTime)
                ? LocalDateTime.of(overnightDate.plusDays(1), overnightEndTime)
                : LocalDateTime.of(overnightDate, overnightEndTime);

        // Tìm phần giao nhau giữa [start-end] và [overnightStart-overnightEnd]
        LocalDateTime overlapStart = start.isAfter(overnightStart) ? start : overnightStart;
        LocalDateTime overlapEnd = end.isBefore(overnightEnd) ? end : overnightEnd;

        Duration overlap = Duration.between(overlapStart, overlapEnd);
        Duration total = Duration.between(start, end);

        // Nếu phần giao nhau >= 6 tiếng -> qua đêm
        return !overlap.isNegative() && overlap.toHours() >= 6;
    }

    private double calculateHourlyPrice(RoomPricing pricing, double hours, boolean isFirst) {
        if (isFirst) {
            if (hours <= pricing.getBaseDuration()) {
                return pricing.getBasePrice();
            } else {
                double extraHours = hours - pricing.getBaseDuration();
                return pricing.getBasePrice() + (pricing.getExtraHourPrice() * extraHours);
            }
        }
        return pricing.getExtraHourPrice() * hours;
    }

    public List<Booking> findBookingsToSendReminder(LocalDateTime start, LocalDateTime end) {
        return bookingRepository.findByCheckInBetweenAndHasSentReminderFalseAndStatus(start, end,
                BookingStatus.CONFIRMED);
    }

}
