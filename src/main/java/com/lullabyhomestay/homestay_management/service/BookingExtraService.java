package com.lullabyhomestay.homestay_management.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.BookingServices;
import com.lullabyhomestay.homestay_management.domain.Customer;
import com.lullabyhomestay.homestay_management.domain.dto.SearchBookingServiceCriteriaDTO;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.BookingServiceRepository;
import com.lullabyhomestay.homestay_management.repository.PaymentDetailRepository;
import com.lullabyhomestay.homestay_management.service.specifications.BookingServiceSpecification;
import com.lullabyhomestay.homestay_management.utils.Constants;
import com.lullabyhomestay.homestay_management.utils.DiscountUtil;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@org.springframework.stereotype.Service
public class BookingExtraService {

    private final BookingServiceRepository bookingServiceRepo;
    private final BookingService bookingService;
    private final PaymentDetailRepository paymentDetailRepo;

    @Transactional
    public BookingServices handleSaveBookingServiceExtra(BookingServices bService) {

        Booking currentBooking = this.bookingService.getBookingById(bService.getBooking().getBookingId());
        Customer customer = currentBooking.getCustomer();

        if (bService.getService().getIsPrepaid() || bService.getQuantity() != null) {
            Double rawAmount = bService.getRawTotalAmount();
            Double discount = DiscountUtil.calculateDiscountAmount(rawAmount, customer);
            Double finalAmount = rawAmount - discount;

            currentBooking.setTotalAmount(currentBooking.getTotalAmount() + finalAmount);
            bookingService.handleSaveBooking(currentBooking);
        }

        return bookingServiceRepo.save(bService);
    }

    public Page<BookingServices> searchBookingServices(SearchBookingServiceCriteriaDTO criteria, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE,
                "asc".equals(criteria.getSort()) ? Sort.by("CreatedAt").ascending()
                        : "desc".equals(criteria.getSort()) ? Sort.by("CreatedAt").descending() : Sort.unsorted());
        boolean isAllCriteriaEmpty = (criteria.getKeyword() == null || criteria.getKeyword().isEmpty())
                && criteria.getIsPrepaid() == null && criteria.getStatus() == null;
        if (isAllCriteriaEmpty) {
            return bookingServiceRepo.findAll(pageable);
        }

        Specification<BookingServices> spec = Specification
                .where(BookingServiceSpecification.serviceNameLike(criteria.getKeyword()))
                .and(BookingServiceSpecification.isAdditionalEqual(criteria.getIsPrepaid()))
                .and(BookingServiceSpecification.statusEqual(criteria.getStatus()));
        return bookingServiceRepo.findAll(spec, pageable);
    }

    public BookingServices getBookingServiceById(Long id) {
        Optional<BookingServices> bookingServiceOpt = bookingServiceRepo.findByBookingServiceId(id);
        if (!bookingServiceOpt.isPresent()) {
            throw new NotFoundException("Việc đặt dịch vụ");
        }
        return bookingServiceOpt.get();
    }

    public List<BookingServices> getListBookingServiceByBookingId(Long bookingId) {
        return bookingServiceRepo.findByBooking_BookingId(bookingId);
    }

    @Transactional
    public void updateQuantityBookingServices(BookingServices bService, Float newQuantity) {
        Booking currentBooking = bookingService.getBookingById(bService.getBooking().getBookingId());
        Customer customer = bService.getBooking().getCustomer();
        BookingServices currentBookingService = getBookingServiceById(bService.getBookingServiceId());

        Double oldTotalPriceService = 0.0;
        if (currentBookingService.getQuantity() != null) {
            oldTotalPriceService = currentBookingService.getRawTotalAmount() - DiscountUtil
                    .calculateDiscountAmount(currentBookingService.getRawTotalAmount(), customer);
        }

        // Tránh tình trạng bị Hibernate cache sai dữ liệu cũ
        bService.setQuantity(newQuantity);
        Double rawServiceTotalAmount = bService.getRawTotalAmount();
        Double discountAmount = DiscountUtil.calculateDiscountAmount(rawServiceTotalAmount, customer);

        Double currentTotal = currentBooking.getTotalAmount();
        Double newPriceService = rawServiceTotalAmount - discountAmount;
        Double totalAmount = currentTotal - oldTotalPriceService + newPriceService;

        currentBooking.setTotalAmount(totalAmount);
        currentBooking = bookingService.handleSaveBooking(currentBooking);

        bookingServiceRepo.save(bService);
    }

    public boolean canUpdateAndDeleteBookingService(Long bookingServiceId) {
        boolean hasPaid = paymentDetailRepo.existsByBookingService_BookingServiceId(bookingServiceId);
        return !hasPaid;
    }

    @Transactional
    public void deleteBookingServiceById(Long bookingServiceId) {
        BookingServices currentBService = getBookingServiceById(bookingServiceId);
        if (currentBService.getQuantity() != null) {
            Booking currentBooking = currentBService.getBooking();
            Customer customer = currentBService.getBooking().getCustomer();

            Double oldTotalPriceService = currentBService.getRawTotalAmount() - DiscountUtil
                    .calculateDiscountAmount(currentBService.getRawTotalAmount(), customer);

            Double currentTotal = currentBooking.getTotalAmount();
            Double newPriceService = currentTotal - oldTotalPriceService;

            currentBooking.setTotalAmount(newPriceService);
            currentBooking = bookingService.handleSaveBooking(currentBooking);
        }
        if (canUpdateAndDeleteBookingService(bookingServiceId)) {
            this.bookingServiceRepo.deleteByBookingServiceId(bookingServiceId);
        }
    }

    public Double calculateUnpaidServicesTotalAmount(Long bookingId) {
        List<BookingServices> unpaidServices = bookingServiceRepo
                .findBookingServicesWithoutPaymentDetail(bookingId);
        if (unpaidServices.isEmpty()) {
            return 0.0;
        }
        Customer customer = unpaidServices.get(0).getBooking().getCustomer();
        Double totalAmount = 0.0;
        for (BookingServices bService : unpaidServices) {
            if (bService.getQuantity() != null)
                totalAmount += bService.getRawTotalAmount();
        }
        totalAmount = totalAmount - DiscountUtil.calculateDiscountAmount(totalAmount, customer);
        return totalAmount;
    }

    @Transactional
    public void deleteByBookingId(Long bookingId) {
        List<BookingServices> bookingServices = getListBookingServiceByBookingId(bookingId);
        for (BookingServices bService : bookingServices) {
            deleteBookingServiceById(bService.getBookingServiceId());
        }
    }

    public boolean allPostpaidServicesHaveQuantity(Long bookingId) {
        return !bookingServiceRepo.existsPostpaidServiceWithoutQuantity(bookingId);
    }

    public void handleUpdateStatusBookingService(BookingServices bService) {
        bService.setUpdatedAt(LocalDateTime.now());
        bookingServiceRepo.save(bService);
    }

    public List<BookingServices> getPrepaidServices(long bookingId) {
        return bookingServiceRepo.findPrepaidServicesByBookingId(bookingId);
    }

    public List<BookingServices> getPostpaidServices(long bookingId) {
        return bookingServiceRepo.findPostpaidServicesByBookingId(bookingId);
    }

}
