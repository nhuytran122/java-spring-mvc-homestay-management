package com.lullabyhomestay.homestay_management.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lullabyhomestay.homestay_management.domain.RoomStatusHistory;

public interface RoomStatusHistoryRepository extends JpaRepository<RoomStatusHistory, Long> {
       Page<RoomStatusHistory> findAll(Pageable page);

       Optional<RoomStatusHistory> findByRoomStatusID(long id);

       RoomStatusHistory save(RoomStatusHistory roomStatusHistory);

       void deleteByRoom_RoomID(long id);

       @Query("SELECT CASE WHEN COUNT(rsh) > 0 THEN true ELSE false END " +
                     "FROM RoomStatusHistory rsh " +
                     "WHERE rsh.room.roomID = :roomId " +
                     "AND (rsh.startedAt < :checkOut AND rsh.endedAt > :checkIn)")
       boolean existsOverlappingStatuses(
                     @Param("roomId") Long roomId,
                     @Param("checkIn") LocalDateTime checkIn,
                     @Param("checkOut") LocalDateTime checkOut);

       void deleteByBooking_BookingID(Long bookingID);
}
