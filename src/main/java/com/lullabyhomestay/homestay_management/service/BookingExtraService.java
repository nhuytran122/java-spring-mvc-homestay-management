package com.lullabyhomestay.homestay_management.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.BookingServices;
import com.lullabyhomestay.homestay_management.domain.Customer;
import com.lullabyhomestay.homestay_management.domain.Service;
import com.lullabyhomestay.homestay_management.domain.dto.CustomerDTO;
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
    private final ModelMapper mapper;
    private final CustomerService customerService;
    private final HomestayServiceService service;

    @Transactional
    public BookingServices handleSaveBookingServiceExtra(BookingServices bookingService) {

        Booking currentBooking = this.bookingService.getBookingByID(bookingService.getBooking().getBookingID());

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
        bookingService.setTotalPrice(totalPrice);

        Double totalAmount = currentBooking.getTotalAmount() + bookingService.getTotalPrice() - oldTotalPrice;
        currentBooking.setTotalAmount(totalAmount);
        currentBooking = this.bookingService.handleSaveBooking(currentBooking);

        // Cập nhật RewardPoints
        Customer customer = currentBooking.getCustomer();
        Double rewardPoints = customer.getRewardPoints();
        Double addPoints = (bookingService.getTotalPrice() / 100000) * 10 - (oldTotalPrice / 100000) * 10;
        customer.setRewardPoints(rewardPoints + addPoints);

        // Cập nhật lại CustomerType
        CustomerDTO customerDTO = mapper.map(customer, CustomerDTO.class);
        customerService.handleSaveCustomer(customerDTO);

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
