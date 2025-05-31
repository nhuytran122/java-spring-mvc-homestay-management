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
import com.lullabyhomestay.homestay_management.utils.RoomStatus;

public interface RoomStatusHistoryRepository extends JpaRepository<RoomStatusHistory, Long> {
       Page<RoomStatusHistory> findAll(Pageable page);

       Optional<RoomStatusHistory> findByRoomStatusId(long id);

       RoomStatusHistory save(RoomStatusHistory roomStatusHistory);

       void deleteByRoom_RoomId(long id);

       @Query("SELECT CASE WHEN COUNT(rsh) > 0 THEN true ELSE false END " +
                     "FROM RoomStatusHistory rsh " +
                     "WHERE rsh.room.roomId = :roomId " +
                     "AND (rsh.startedAt < :checkOut AND rsh.endedAt > :checkIn)")
       boolean existsOverlappingStatuses(
                     @Param("roomId") Long roomId,
                     @Param("checkIn") LocalDateTime checkIn,
                     @Param("checkOut") LocalDateTime checkOut);

       void deleteByBooking_BookingId(Long bookingId);

       // @Modifying
       // @Query("DELETE FROM RoomStatusHistory rs WHERE rs.booking.bookingId =
       // :bookingId")
       // void deleteByBooking_BookingId(@Param("bookingId") Long bookingId);

       List<RoomStatusHistory> findByRoom_RoomIdAndStartedAtBetween(Long roomId, LocalDateTime startOfDate,
                     LocalDateTime endOfDate);

       Optional<RoomStatusHistory> findByBooking_BookingIdAndStatus(Long bookingId, RoomStatus roomStatus);
}
