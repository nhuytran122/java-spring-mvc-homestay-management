package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.RoomType;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.RoomRepository;
import com.lullabyhomestay.homestay_management.repository.RoomTypeRepository;
import com.lullabyhomestay.homestay_management.utils.Constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;

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

    public Page<RoomType> searchRoomTypes(String keyword, int page, String sortOrder) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE,
                "asc".equals(sortOrder) ? Sort.by("PricePerHour").ascending()
                        : "desc".equals(sortOrder) ? Sort.by("PricePerHour").descending() : Sort.unsorted());

        return (keyword != null && !keyword.isEmpty())
                ? this.roomTypeRepository.findAll(pageable)
                : this.roomTypeRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    public RoomType handleSaveRoomType(RoomType roomType) {
        return roomTypeRepository.save(roomType);
    }

    public boolean canDeleteRoomType(long typeID) {
        boolean hasRooms = roomRepository.existsByRoomType_RoomTypeID(typeID);
        return !(hasRooms);
    }

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
