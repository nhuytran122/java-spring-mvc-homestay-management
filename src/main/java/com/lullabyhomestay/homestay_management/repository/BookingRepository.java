package com.lullabyhomestay.homestay_management.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lullabyhomestay.homestay_management.domain.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long>{
    Page<Booking> findAll(Pageable page);

    Optional<Booking> findByBookingID(long bookingID);

    Booking save(Booking booking);

    void deleteByBookingID(long id);

    boolean existsByRoom_RoomID(long roomID);
}
