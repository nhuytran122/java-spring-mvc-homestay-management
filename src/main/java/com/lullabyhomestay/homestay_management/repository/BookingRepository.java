package com.lullabyhomestay.homestay_management.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
        Page<Booking> findAll(Pageable page);

        Page<Booking> findAll(Specification<Booking> spec, Pageable page);

        @EntityGraph(attributePaths = { "room", "room.roomType", "room.branch",
                        "customer", "payments" })
        Optional<Booking> findByBookingId(long bookingId);

        List<Booking> findByCustomer_CustomerId(Long customerId);

        Booking save(Booking booking);

        void deleteByBookingId(long id);

        boolean existsByRoom_RoomId(long roomId);

        boolean existsByCustomer_CustomerId(Long customerId);

        Long countByStatusAndCustomer_CustomerId(BookingStatus bookingStatus, Long customerId);

        Long countByCustomer_CustomerId(Long customerId);

        // Double findSumTotalAmountByCustomer_CustomerId(Long customerId);
        @Query("SELECT SUM(b.totalAmount) FROM Booking b " +
                        "WHERE b.customer.id = :customerId " +
                        "AND b.status = 'COMPLETED' ")

        Double getTotalAmountByCustomerId(@Param("customerId") Long customerId);

        List<Booking> findByStatus(BookingStatus status);

        // @Query(value = "SELECT * FROM Bookings WHERE status = 'PENDING' AND CreatedAt
        // < DATEADD(MINUTE, -:timeoutMinutes, GETDATE())", nativeQuery = true)
        // List<Booking> findPendingBookingsBefore(@Param("timeoutMinutes") int
        // timeoutMinutes);

        @Query(value = "SELECT * FROM Bookings WHERE status = 'PENDING' AND created_at < DATE_SUB(NOW(), INTERVAL :timeoutMinutes MINUTE)", nativeQuery = true)
        List<Booking> findPendingBookingsBefore(@Param("timeoutMinutes") int timeoutMinutes);

        @Query("SELECT COUNT(b) FROM Booking b WHERE b.createdAt BETWEEN :startDate AND :endDate")
        Long countBookingsByDateRange(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        List<Booking> findByCheckInBetweenAndHasSentReminderFalseAndStatus(
                        LocalDateTime start, LocalDateTime end, BookingStatus status);

}
