package com.lullabyhomestay.homestay_management.repository;

import com.lullabyhomestay.homestay_management.domain.Review;
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
    //            "JOIN Bookings b ON r.BookingID = b.BookingID " +
    //            "JOIN Rooms rm ON b.RoomID = rm.RoomID " +
    //            "WHERE rm.RoomID = :roomId", 
    //    nativeQuery = true)
    // List<Review> findByRoomId(@Param("roomId") Long roomId);


    @Query("SELECT rv FROM Review rv " +
           "JOIN rv.booking b " +
           "JOIN b.room r " +
           "WHERE r.roomID = :roomId")
    List<Review> findByRoomID(@Param("roomId") Long roomId);
}