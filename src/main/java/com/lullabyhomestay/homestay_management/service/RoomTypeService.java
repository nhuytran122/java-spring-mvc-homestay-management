package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.RoomPricing;
import com.lullabyhomestay.homestay_management.domain.RoomType;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.RoomPricingRepository;
import com.lullabyhomestay.homestay_management.repository.RoomRepository;
import com.lullabyhomestay.homestay_management.repository.RoomTypeRepository;
import com.lullabyhomestay.homestay_management.utils.Constants;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final RoomPricingRepository roomPricingRepository;

    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAll();
    }

    public RoomType getRoomTypeById(long id) {
        Optional<RoomType> roomTypeOpt = roomTypeRepository.findByRoomTypeID(id);
        if (!roomTypeOpt.isPresent()) {
            throw new NotFoundException("Loại phòng");
        }
        return roomTypeOpt.get();
    }

    public Page<RoomType> searchRoomTypes(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE);

        return (keyword != null && !keyword.isEmpty())
                ? this.roomTypeRepository.findAll(pageable)
                : this.roomTypeRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    @Transactional
    public void createRoomType(RoomType rType) {
        RoomType roomType = roomTypeRepository.save(rType);

        RoomPricing providedPricing = (rType.getRoomPricings() != null && !rType.getRoomPricings().isEmpty())
                ? rType.getRoomPricings().get(0)
                : null;

        if (providedPricing != null) {
            RoomPricing roomPricing = new RoomPricing();
            roomPricing.setRoomType(roomType);
            roomPricing.setIsDefault(true);
            copyPricingFields(providedPricing, roomPricing);

            roomPricingRepository.save(roomPricing);
        } else {
            throw new IllegalArgumentException("Thiếu thông tin RoomPricing mặc định.");
        }
    }

    @Transactional
    public void updateRoomType(RoomType rType) {
        RoomType roomType = getRoomTypeById(rType.getRoomTypeID());

        Optional<RoomPricing> existsPricingOpt = roomPricingRepository
                .findFirstByRoomType_RoomTypeIDAndIsDefaultTrue(roomType.getRoomTypeID());

        RoomPricing providedPricing = (rType.getRoomPricings() != null && !rType.getRoomPricings().isEmpty())
                ? rType.getRoomPricings().get(0)
                : null;
        RoomPricing roomPricing;
        if (existsPricingOpt.isPresent()) {
            roomPricing = existsPricingOpt.get();
        } else {
            roomPricing = new RoomPricing();
            roomPricing.setRoomType(roomType);
        }
        if (providedPricing != null) {
            copyPricingFields(providedPricing, roomPricing);
            roomPricingRepository.save(roomPricing);
        }
        roomTypeRepository.save(roomType);
    }

    private void copyPricingFields(RoomPricing from, RoomPricing to) {
        to.setBaseDuration(from.getBaseDuration());
        to.setBasePrice(from.getBasePrice());
        to.setDailyPrice(from.getDailyPrice());
        to.setExtraHourPrice(from.getExtraHourPrice());
        to.setOvernightPrice(from.getOvernightPrice());
    }

    public boolean canDeleteRoomType(long typeID) {
        boolean hasRooms = roomRepository.existsByRoomType_RoomTypeID(typeID);
        return !(hasRooms);
    }

    @Transactional
    public void deleteByRoomTypeID(long id) {
        if (canDeleteRoomType(id)) {
            roomTypeRepository.deleteByRoomTypeID(id);
        }
    }

    public boolean existsByName(String name) {
        return roomTypeRepository.existsByNameIgnoreCase(name.trim());
    }

    public boolean existsByNameAndNotId(String name, Long id) {
        return roomTypeRepository.existsByNameIgnoreCaseAndRoomTypeIDNot(name.trim(), id);
    }

}
