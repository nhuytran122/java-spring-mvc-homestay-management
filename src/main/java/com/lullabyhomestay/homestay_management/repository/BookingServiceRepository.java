package com.lullabyhomestay.homestay_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.BookingServices;

@Repository
public interface BookingServiceRepository extends JpaRepository<BookingServices, Long> {
    boolean existsByService_ServiceID(long serviceID);

    BookingServices save(BookingServices bookingService);

}
