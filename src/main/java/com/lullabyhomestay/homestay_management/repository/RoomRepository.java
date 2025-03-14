package com.lullabyhomestay.homestay_management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.lullabyhomestay.homestay_management.domain.Room;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {

        Page<Room> findAll(Pageable page);

        Page<Room> findAll(Specification<Room> spec, Pageable page);

        Optional<Room> findByRoomID(long roomID);

        List<Room> findByBranch_BranchID(long branchID);

        Room save(Room room);

        void deleteByRoomID(long roomID);

        boolean existsByBranch_BranchID(long branchID);

        boolean existsByRoomType_RoomTypeID(long roomType);

        @Query(value = "SELECT r.* FROM Rooms r " +
                        "WHERE NOT EXISTS (" +
                        "    SELECT 1 FROM Bookings b " +
                        "    LEFT JOIN (" +
                        "        SELECT be.BookingID, COALESCE(SUM(be.ExtraHours), 0) AS TotalExtraHours " +
                        "        FROM BookingExtensions be " +
                        "        GROUP BY be.BookingID" +
                        "    ) AS ext ON b.BookingID = ext.BookingID " +
                        "    WHERE b.RoomID = r.RoomID " +
                        "    AND b.CheckIn < :endTime " +
                        "    AND DATEADD(hour, ext.TotalExtraHours, b.CheckOut) > :startTime" +
                        ") " +
                        "AND (:branchId IS NULL OR r.BranchID = :branchId) " +
                        "AND (:roomTypeId IS NULL OR r.RoomTypeID = :roomTypeId) " +
                        "AND (r.IsActive = 1)",

                        countQuery = "SELECT COUNT(r.RoomID) FROM Rooms r " +
                                        "WHERE NOT EXISTS (" +
                                        "    SELECT 1 FROM Bookings b " +
                                        "    LEFT JOIN (" +
                                        "        SELECT be.BookingID, COALESCE(SUM(be.ExtraHours), 0) AS TotalExtraHours "
                                        +
                                        "        FROM BookingExtensions be " +
                                        "        GROUP BY be.BookingID" +
                                        "    ) AS ext ON b.BookingID = ext.BookingID " +
                                        "    WHERE b.RoomID = r.RoomID " +
                                        "    AND b.CheckIn < :endTime " +
                                        "    AND DATEADD(hour, ext.TotalExtraHours, b.CheckOut) > :startTime" +
                                        ") " +
                                        "AND (:branchId IS NULL OR r.BranchID = :branchId) " +
                                        "AND (:roomTypeId IS NULL OR r.RoomTypeID = :roomTypeId) " +
                                        "AND (r.IsActive = 1)", nativeQuery = true)
        Page<Room> findAvailableRooms(
                        @Param("branchId") Long branchId,
                        @Param("roomTypeId") Long roomTypeId,
                        @Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime,
                        Pageable pageable);

}
