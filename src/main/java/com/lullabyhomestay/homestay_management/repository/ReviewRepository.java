package com.lullabyhomestay.homestay_management.repository;

import com.lullabyhomestay.homestay_management.domain.Review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByReviewID(Long reviewID);

    void deleteByReviewID(Long reviewID);

    // @Query(value = "SELECT r.* FROM Reviews r " +
    // "JOIN Bookings b ON r.BookingID = b.BookingID " +
    // "JOIN Rooms rm ON b.RoomID = rm.RoomID " +
    // "WHERE rm.RoomID = :roomId",
    // nativeQuery = true)
    // List<Review> findByRoomId(@Param("roomId") Long roomId);

    @Query("SELECT rv FROM Review rv " +
            "JOIN rv.booking b " +
            "JOIN b.room r " +
            "WHERE r.roomID = :roomId")
    List<Review> findByRoomID(@Param("roomId") Long roomId);

    List<Review> findTop10ByRatingOrderByCreatedAtDesc(int rating);

    Page<Review> findAll(Specification<Review> spec, Pageable page);

    @Query("SELECT rv FROM Review rv " +
            "JOIN rv.booking b " +
            "JOIN b.room r " +
            "JOIN r.branch br " +
            "WHERE br.branchID = :branchId")
    Page<Review> findByBranchID(@Param("branchId") Long branchId, Pageable pageable);

}