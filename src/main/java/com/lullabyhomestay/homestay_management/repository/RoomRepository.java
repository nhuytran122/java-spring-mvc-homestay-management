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

        Page<Room> findAll(Specification<Room> spec, Pageable page);

        @EntityGraph(attributePaths = { "roomType", "roomAmenities", "branch" })
        Optional<Room> findByRoomId(long roomId);

        List<Room> findByBranch_BranchId(long branchId);

        Room save(Room room);

        void deleteByRoomId(long roomId);

        boolean existsByBranch_BranchId(long branchId);

        boolean existsByRoomType_RoomTypeId(long roomType);

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
                        SELECT TOP 5 r.RoomId, r.RoomNumber, br.BranchName, rt.name, COUNT(b.BookingId) AS bookingCount
                        FROM Rooms r
                        LEFT JOIN Bookings b ON b.RoomId = r.RoomId
                        JOIN Branches br ON r.BranchId = br.BranchId
                        JOIN RoomTypes rt ON r.RoomTypeId = rt.RoomTypeId
                        WHERE b.CheckIn BETWEEN :startDate AND :endDate
                        GROUP BY r.RoomId, r.RoomNumber, br.BranchName, rt.name
                        ORDER BY bookingCount DESC
                        """, nativeQuery = true)
        List<Object[]> findTop5RoomsWithCount(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        // JPQL
        @Query("""
                            SELECT r.roomNumber, r.branch.branchName, r.roomType.name, COUNT(b) as bookingCount
                            FROM Room r
                            LEFT JOIN Booking b ON b.room = r
                            WHERE b.checkIn BETWEEN :startDate AND :endDate
                            GROUP BY r.roomNumber, r.branch.branchName, r.roomType.name
                            ORDER BY bookingCount DESC
                        """)
        List<Object[]> findTopRoomsWithCount(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate,
                        Pageable pageable);

}
