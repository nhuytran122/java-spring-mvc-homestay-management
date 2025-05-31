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

import com.lullabyhomestay.homestay_management.domain.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
        List<Service> findAll();

        List<Service> findByIsPrepaid(Boolean isPrepaid);

        Page<Service> findAll(Pageable page);

        Page<Service> findByServiceNameContainingIgnoreCase(
                        String serviceName, Pageable pageable);

        Optional<Service> findByServiceId(long serviceId);

        Service save(Service service);

        void deleteByServiceId(long serviceId);

        @Query(value = """
                        SELECT TOP 5 s.ServiceId, s.ServiceName, COUNT(bs.bookingServiceId) AS serviceCount
                        FROM Services s
                        LEFT JOIN BookingServices bs ON bs.serviceId = s.ServiceId
                        LEFT JOIN Bookings b ON b.bookingId = bs.BookingId
                        WHERE b.checkIn BETWEEN :startDate AND :endDate
                        GROUP BY s.ServiceId, s.ServiceName
                        ORDER BY serviceCount DESC
                        """, nativeQuery = true)
        List<Object[]> findTopServicesWithCount(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        // JPQL
        @Query("""
                            SELECT s.serviceName, COUNT(bs.bookingServiceId) AS serviceCount
                            FROM Service s
                            LEFT JOIN s.bookingServices bs
                            LEFT JOIN bs.booking b
                            WHERE b.checkIn BETWEEN :startDate AND :endDate
                            GROUP BY s.serviceName
                            ORDER BY serviceCount DESC
                        """)
        List<Object[]> findTopServicesWithCount(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate,
                        Pageable pageable);

}
