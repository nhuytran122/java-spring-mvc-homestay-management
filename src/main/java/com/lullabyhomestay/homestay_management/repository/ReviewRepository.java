package com.lullabyhomestay.homestay_management.repository;

import com.lullabyhomestay.homestay_management.domain.Review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

        Optional<Review> findByReviewId(Long reviewId);

        void deleteByReviewId(Long reviewId);

        // @Query(value = "SELECT r.* FROM Reviews r " +
        // "JOIN Bookings b ON r.BookingId = b.BookingId " +
        // "JOIN Rooms rm ON b.RoomId = rm.RoomId " +
        // "WHERE rm.RoomId = :roomId",
        // nativeQuery = true)
        // List<Review> findByRoomId(@Param("roomId") Long roomId);

        @Query("SELECT rv FROM Review rv " +
                        "JOIN rv.booking b " +
                        "JOIN b.room r " +
                        "WHERE r.roomId = :roomId")
        List<Review> findByRoomId(@Param("roomId") Long roomId);

        List<Review> findTop10ByRatingOrderByCreatedAtDesc(int rating);

        Page<Review> findAll(Specification<Review> spec, Pageable page);

        @Query("SELECT rv FROM Review rv " +
                        "JOIN rv.booking b " +
                        "JOIN b.room r " +
                        "JOIN r.branch br " +
                        "WHERE br.branchId = :branchId")
        Page<Review> findByBranchId(@Param("branchId") Long branchId, Pageable pageable);

        @Query("SELECT COUNT(r) FROM Review r WHERE r.createdAt BETWEEN :startDate AND :endDate")
        Long countReviews(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}