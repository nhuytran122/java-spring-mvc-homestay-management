package com.lullabyhomestay.homestay_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
    Page<Booking> findAll(Pageable page);

    Page<Booking> findAll(Specification<Booking> spec, Pageable page);

    Optional<Booking> findByBookingID(long bookingID);

    List<Booking> findByCustomer_CustomerID(Long customerID);

    Booking save(Booking booking);

    void deleteByBookingID(long id);

    boolean existsByRoom_RoomID(long roomID);

    boolean existsByCustomer_CustomerID(Long customerID);

    Long countByStatusAndCustomer_CustomerID(BookingStatus bookingStatus, Long customerID);

    Long countByCustomer_CustomerID(Long customerID);
}
