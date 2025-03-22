package com.lullabyhomestay.homestay_management.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.Customer;
import com.lullabyhomestay.homestay_management.domain.dto.SearchBookingCriteriaDTO;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.BookingRepository;
import com.lullabyhomestay.homestay_management.repository.BookingServiceRepository;
import com.lullabyhomestay.homestay_management.repository.RoomStatusHistoryRepository;
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
    private final ModelMapper mapper;
    private final BookingServiceRepository bookingServiceRepo;
    private final RoomStatusHistoryRepository roomStatusHistoryRepo;

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
        Double totalAmount = calculateTotalAmount(booking, booking.getCustomer());
        booking.setTotalAmount(totalAmount);
        Booking savedBooking = bookingRepository.save(booking);
        roomStatusHistoryService.handleStatusWhenBooking(savedBooking);

        customerService.updateRewardPointsAndCustomerType(
                booking.getCustomer().getCustomerID(),
                savedBooking.getTotalAmount(),
                true);
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

    public boolean canCancelBooking(Long bookingID) {
        Booking booking = getBookingByID(bookingID);
        LocalDateTime checkInTime = booking.getCheckIn();
        LocalDateTime now = LocalDateTime.now();
        long hoursDifference = ChronoUnit.MINUTES.between(now, checkInTime);
        return hoursDifference >= 24 * 60;
    }

    @Transactional
    public void cancelBooking(Long bookingID) {
        if (canCancelBooking(bookingID)) {
            roomStatusHistoryRepo.deleteByBooking_BookingID(bookingID);
            bookingServiceRepo.deleteByBooking_BookingID(bookingID);
            Booking currentBooking = getBookingByID(bookingID);
            currentBooking.setStatus(BookingStatus.CANCELLED);
            customerService.updateRewardPointsAndCustomerType(
                    currentBooking.getCustomer().getCustomerID(),
                    currentBooking.getTotalAmount(),
                    false);
        }
    }

    private double calculateTotalAmount(Booking booking, Customer customer) {
        long hours = booking.getNumberOfHours();
        Double pricePerHour = booking.getRoom().getRoomType().getPricePerHour();
        double totalAmount = pricePerHour * hours;
        if (booking.getRoom().getRoomType().getName().toUpperCase().contains("DORM")) { 
            totalAmount *= booking.getGuestCount();
        }        
        if (customer.getCustomerType().getDiscountRate() > 0) {
            totalAmount = totalAmount * (100 - customer.getCustomerType().getDiscountRate()) / 100;
        }
        return totalAmount;
    }
}
