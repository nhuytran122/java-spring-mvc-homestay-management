package com.lullabyhomestay.homestay_management.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.BookingServices;

@Repository
public interface BookingServiceRepository extends JpaRepository<BookingServices, Long> {
    Page<BookingServices> findAll(Pageable page);

    Page<BookingServices> findAll(Specification<BookingServices> spec, Pageable page);

    Optional<BookingServices> findByBookingServiceID(long bookingID);

    boolean existsByService_ServiceID(long serviceID);

    BookingServices save(BookingServices bookingService);

    void deleteByBooking_BookingID(Long bookingID);

}
