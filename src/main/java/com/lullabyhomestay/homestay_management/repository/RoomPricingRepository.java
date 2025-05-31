package com.lullabyhomestay.homestay_management.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.RoomPricing;

@Repository
public interface RoomPricingRepository extends JpaRepository<RoomPricing, Long> {
    List<RoomPricing> findByRoomType_RoomTypeId(Long roomTypeId);

    Optional<RoomPricing> findFirstByRoomType_RoomTypeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long roomTypeId, LocalDate checkInDate, LocalDate checkOutDate);

    Optional<RoomPricing> findFirstByRoomType_RoomTypeIdAndIsDefaultTrue(Long roomTypeId);

    @Query("""
                SELECT COUNT(rp) > 0 FROM RoomPricing rp
                WHERE rp.roomType.roomTypeId = :roomTypeId
                AND (:currentId IS NULL OR rp.roomPricingId <> :currentId)
                AND (
                    (rp.startDate IS NULL OR rp.startDate <= :endDate)
                    AND (rp.endDate IS NULL OR rp.endDate >= :startDate)
                )
                    AND (rp.isDefault = false)

            """)
    boolean isOverlapping(
            @Param("roomTypeId") Long roomTypeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("currentId") Long currentId);

    @Modifying
    @Query("""
                UPDATE RoomPricing rp
                SET rp.isDefault = false
                WHERE rp.roomType.roomTypeId = :roomTypeId
                AND (:currentId IS NULL OR rp.roomPricingId <> :currentId)
            """)
    void clearDefaultForRoomType(@Param("roomTypeId") Long roomTypeId,
            @Param("currentId") Long currentId);
}
