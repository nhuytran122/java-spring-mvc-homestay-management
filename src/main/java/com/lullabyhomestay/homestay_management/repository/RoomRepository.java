package com.lullabyhomestay.homestay_management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
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

        @EntityGraph(attributePaths = { "roomType", "roomAmenities", "branch" })
        Page<Room> findAll(Specification<Room> spec, Pageable page);

        @EntityGraph(attributePaths = { "roomType", "roomAmenities", "branch" })
        Optional<Room> findByRoomID(long roomID);

        List<Room> findByBranch_BranchID(long branchID);

        Room save(Room room);

        void deleteByRoomID(long roomID);

        boolean existsByBranch_BranchID(long branchID);

        boolean existsByRoomType_RoomTypeID(long roomType);

        @Query(value = """
                        SELECT r FROM Room r
                        WHERE r.isActive = true
                        AND (:branchId IS NULL OR r.branch.id = :branchId)
                        AND (:roomTypeId IS NULL OR r.roomType.id = :roomTypeId)
                        ORDER BY r.roomNumber ASC
                        """)
        Page<Room> findAvailableRooms(
                        @Param("branchId") Long branchId,
                        @Param("roomTypeId") Long roomTypeId,
                        Pageable pageable);

        @Query(value = """
                        SELECT TOP 5 r.RoomID, r.RoomNumber, br.BranchName, rt.name, COUNT(b.BookingID) AS bookingCount
                        FROM Rooms r
                        LEFT JOIN Bookings b ON b.RoomID = r.RoomID
                        JOIN Branches br ON r.BranchID = br.BranchID
                        JOIN RoomTypes rt ON r.RoomTypeID = rt.RoomTypeID
                        WHERE b.CheckIn BETWEEN :startDate AND :endDate
                        GROUP BY r.RoomID, r.RoomNumber, br.BranchName, rt.name
                        ORDER BY bookingCount DESC
                        """, nativeQuery = true)
        List<Object[]> findTop5RoomsWithCount(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

}
