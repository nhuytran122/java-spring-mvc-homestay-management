package com.lullabyhomestay.homestay_management.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.RoomPricing;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.RoomPricingRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoomPricingService {
    private final RoomPricingRepository roomPricingRepository;

    public Optional<RoomPricing> getDefaultRoomPricing(Long id) {
        Optional<RoomPricing> roomPricingOpt = roomPricingRepository.findFirstByRoomType_RoomTypeIDAndIsDefaultTrue(id);
        return roomPricingOpt;
    }

    public RoomPricing getRoomPricingByID(Long id) {
        Optional<RoomPricing> roomPricingOpt = roomPricingRepository.findById(id);
        if (!roomPricingOpt.isPresent()) {
            throw new NotFoundException("Giá phòng");
        }
        return roomPricingOpt.get();
    }

    @Transactional
    public void handleSaveRoomPricing(RoomPricing roomPricing) {
        if (roomPricing.getIsDefault()) {
            roomPricingRepository.clearDefaultForRoomType(
                    roomPricing.getRoomType().getRoomTypeID(),
                    roomPricing.getRoomPricingID());
        }
        roomPricingRepository.save(roomPricing);
    }

    public boolean isOverlapping(LocalDate startDate, LocalDate endDate, Long roomTypeID, Long currentId) {
        return roomPricingRepository.isOverlapping(roomTypeID, startDate, endDate, currentId);
    }

    public void deleteRoomPricingByID(Long id) {
        if (this.canDeleteRoomPricing(id)) {
            roomPricingRepository.deleteById(id);
            return;
        }
        throw new IllegalStateException("Không thể xóa chính sách giá vì đây là mặc định.");
    }

    public boolean canDeleteRoomPricing(Long id) {
        RoomPricing roomPricing = getRoomPricingByID(id);
        return !roomPricing.getIsDefault();
    }

}
