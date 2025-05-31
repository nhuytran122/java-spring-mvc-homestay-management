package com.lullabyhomestay.homestay_management.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.BookingServices;
import com.lullabyhomestay.homestay_management.utils.BookingServiceStatus;

@Repository
public interface BookingServiceRepository extends JpaRepository<BookingServices, Long> {
    Page<BookingServices> findAll(Pageable page);

    Page<BookingServices> findAll(Specification<BookingServices> spec, Pageable page);

    @EntityGraph(attributePaths = { "booking", "service" })
    Optional<BookingServices> findByBookingServiceId(long bookingId);

    List<BookingServices> findByBooking_BookingId(Long bookingId);

    boolean existsByService_ServiceId(long serviceId);

    BookingServices save(BookingServices bookingService);

    boolean existsByBooking_BookingId(Long bookingId);

    void deleteByBookingServiceId(Long bookingServiceId);

    void deleteByBooking_BookingId(Long bookingId);

    @EntityGraph(attributePaths = { "service" })
    @Query("SELECT bs FROM BookingServices bs WHERE bs.booking.bookingId = :bookingId AND bs.service.isPrepaid = true")
    List<BookingServices> findPrepaidServicesByBookingId(@Param("bookingId") Long bookingId);

    @EntityGraph(attributePaths = { "service" })
    @Query("SELECT bs FROM BookingServices bs WHERE bs.booking.bookingId = :bookingId AND bs.service.isPrepaid = false")
    List<BookingServices> findPostpaidServicesByBookingId(@Param("bookingId") Long bookingId);

    @Query("SELECT bs FROM BookingServices bs WHERE bs.booking.bookingId = :bookingId " +
            "AND NOT EXISTS (SELECT pd FROM PaymentDetail pd WHERE pd.bookingService.bookingServiceId = bs.bookingServiceId) "
            +
            "AND bs.booking.status != 'CANCELLED'")
    List<BookingServices> findBookingServicesWithoutPaymentDetail(@Param("bookingId") Long bookingId);

    @Modifying
    @Query("UPDATE BookingServices bs SET bs.status = :status " +
            "WHERE bs.booking.bookingId = :bookingId")
    int bulkUpdateServiceStatusByBookingId(@Param("bookingId") Long bookingId,
            @Param("status") BookingServiceStatus status);

    @Query("""
                SELECT COUNT(bs) > 0
                FROM BookingServices bs
                WHERE bs.booking.bookingId = :bookingId
                  AND bs.service.isPrepaid = false
                  AND (bs.quantity IS NULL OR bs.quantity <= 0)
            """)
    boolean existsPostpaidServiceWithoutQuantity(@Param("bookingId") Long bookingId);

    @Query("""
            SELECT s, COUNT(bs.bookingServiceId) AS serviceCount
            FROM Service s
            LEFT JOIN s.bookingServices bs
            WHERE bs.booking.checkIn BETWEEN :startDate AND :endDate
            GROUP BY s
            ORDER BY serviceCount DESC
            """)
    List<Object[]> findTopServicesWithCount(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("""
                SELECT COUNT(bs)
                FROM BookingServices bs
                WHERE bs.status = 'PENDING'
                AND bs.booking.checkIn BETWEEN :startDate AND :endDate
            """)
    Long countPendingServicesByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

}
