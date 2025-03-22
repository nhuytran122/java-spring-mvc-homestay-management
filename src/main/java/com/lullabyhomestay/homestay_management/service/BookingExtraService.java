package com.lullabyhomestay.homestay_management.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.BookingServices;
import com.lullabyhomestay.homestay_management.domain.Customer;
import com.lullabyhomestay.homestay_management.domain.Service;
import com.lullabyhomestay.homestay_management.domain.dto.SearchBookingServiceCriteriaDTO;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.BookingServiceRepository;
import com.lullabyhomestay.homestay_management.service.specifications.BookingServiceSpecification;
import com.lullabyhomestay.homestay_management.utils.Constants;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@org.springframework.stereotype.Service
public class BookingExtraService {

    private final BookingServiceRepository bookingServiceRepo;
    private final BookingService bookingService;
    private final CustomerService customerService;
    private final HomestayServiceService service;

    @Transactional
    public BookingServices handleSaveBookingServiceExtra(BookingServices bookingService) {

        Booking currentBooking = this.bookingService.getBookingByID(bookingService.getBooking().getBookingID());
        Customer customer = currentBooking.getCustomer();

        Double oldTotalPrice = (double) 0;
        // Với trường hợp sửa số lượng
        if (bookingService.getBookingServiceID() != null) {
            Optional<BookingServices> currentBookingServiceOpt = this.bookingServiceRepo
                    .findByBookingServiceID(bookingService.getBookingServiceID());
            if (currentBookingServiceOpt.isPresent()) {
                oldTotalPrice = currentBookingServiceOpt.get().getTotalPrice();
            }
        }

        Service service = this.service.getServiceByID(bookingService.getService().getServiceID());
        Double totalPrice = service.getPrice() * bookingService.getQuantity();
        if (customer.getCustomerType().getDiscountRate() > 0) {
            totalPrice = totalPrice * (100 - customer.getCustomerType().getDiscountRate()) / 100;
        }
        bookingService.setTotalPrice(totalPrice);

        Double totalAmount = currentBooking.getTotalAmount() + bookingService.getTotalPrice() - oldTotalPrice;
        currentBooking.setTotalAmount(totalAmount);
        currentBooking = this.bookingService.handleSaveBooking(currentBooking);

        // Cập nhật RewardPoints
        Double amountChange = bookingService.getTotalPrice() - oldTotalPrice;
        boolean isAdd = amountChange > 0;
        customerService.updateRewardPointsAndCustomerType(customer.getCustomerID(), amountChange, isAdd);

        return bookingServiceRepo.save(bookingService);
    }

    public Page<BookingServices> searchBookingServices(SearchBookingServiceCriteriaDTO criteria, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE,
                "asc".equals(criteria.getSort()) ? Sort.by("CreatedAt").ascending()
                        : "desc".equals(criteria.getSort()) ? Sort.by("CreatedAt").descending() : Sort.unsorted());
        boolean isAllCriteriaEmpty = (criteria.getKeyword() == null || criteria.getKeyword().isEmpty())
                && criteria.getIsPrepaid() == null;
        if (isAllCriteriaEmpty) {
            return bookingServiceRepo.findAll(pageable);
        }

        Specification<BookingServices> spec = Specification
                .where(BookingServiceSpecification.serviceNameLike(criteria.getKeyword()))
                .and(BookingServiceSpecification.isAdditionalEqual(criteria.getIsPrepaid()));
        return bookingServiceRepo.findAll(spec, pageable);
    }

    public BookingServices getBookingServiceByID(Long id) {
        Optional<BookingServices> bookingServiceOpt = bookingServiceRepo.findByBookingServiceID(id);
        if (!bookingServiceOpt.isPresent()) {
            throw new NotFoundException("Việc đặt dịch vụ");
        }
        return bookingServiceOpt.get();
    }
}
