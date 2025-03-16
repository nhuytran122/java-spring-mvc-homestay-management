package com.lullabyhomestay.homestay_management.service;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.Branch;
import com.lullabyhomestay.homestay_management.domain.dto.SearchBookingCriteriaDTO;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.BookingRepository;
import com.lullabyhomestay.homestay_management.service.specifications.BookingSpecifications;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;
import com.lullabyhomestay.homestay_management.utils.Constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RoomStatusHistoryService roomStatusHistoryService;
    private final CustomerService customerService;

    public Page<Booking> searchBookings(SearchBookingCriteriaDTO criteria, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE,
                "asc".equals(criteria.getSort()) ? Sort.by("CheckIn").ascending()
                        : "desc".equals(criteria.getSort()) ? Sort.by("CheckIn").descending() : Sort.unsorted());
        boolean isAllCriteriaEmpty = (criteria.getKeyword() == null || criteria.getKeyword().isEmpty())
                && criteria.getBranchID() == null
                && criteria.getRoomTypeID() == null
                && (criteria.getStatus() == null || criteria.getStatus().isEmpty())
                && (criteria.getTimeRange() == null || criteria.getTimeRange().isEmpty());

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
                        .and(BookingSpecifications.checkInBetween(criteria.getFromTime(), criteria.getToTime())));
        return bookingRepository.findAll(spec, pageable);
    }

    @Transactional
    public Booking handleBooking(Booking booking) {
        // TODO: Set customer đang login or được chọn bởi admin
        booking.setCustomer(customerService.getCustomerByID(4L));

        long hours = ChronoUnit.HOURS.between(booking.getCheckIn(), booking.getCheckOut());
        double pricePerHour = booking.getRoom().getRoomType().getPricePerHour();
        double totalAmount = pricePerHour * hours;
        booking.setTotalAmount(totalAmount);
        Booking savedBooking = bookingRepository.save(booking);
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
}
