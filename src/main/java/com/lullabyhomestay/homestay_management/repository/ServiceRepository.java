package com.lullabyhomestay.homestay_management.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.BookingServices;
import com.lullabyhomestay.homestay_management.domain.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findAll();

    List<Service> findByIsPrepaid(Boolean isPrepaid);

    Page<Service> findAll(Pageable page);

    Page<Service> findByServiceNameContainingIgnoreCase(
            String serviceName, Pageable pageable);

    Optional<Service> findByServiceID(long serviceID);

    Service save(Service service);

    void deleteByServiceID(long serviceID);

    @Query(value = """
            SELECT TOP 5 s.ServiceID, s.ServiceName, COUNT(bs.bookingServiceID) AS serviceCount
            FROM Services s
            LEFT JOIN BookingServices bs ON bs.serviceID = s.ServiceID
            LEFT JOIN Bookings b ON b.bookingID = bs.BookingID
            WHERE b.checkIn BETWEEN :startDate AND :endDate
            GROUP BY s.ServiceID, s.ServiceName
            ORDER BY serviceCount DESC
            """, nativeQuery = true)
    List<Object[]> findTopServicesWithCount(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

}
