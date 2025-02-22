package com.lullabyhomestay.homestay_management.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.RoomType;
import com.lullabyhomestay.homestay_management.repository.RoomRepository;
import com.lullabyhomestay.homestay_management.repository.RoomTypeRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;

    public Page<RoomType> getAllRoomTypes(Pageable pageable) {
        return roomTypeRepository.findAll(pageable);
    }

    public Optional<RoomType> getRoomTypeById(long id) {
        return roomTypeRepository.findByRoomTypeID(id);
    }

    public Page<RoomType> searchRoomTypesByName(String roomTypeName, Pageable page) {
        return roomTypeRepository.findByNameContainingIgnoreCase(roomTypeName, page);
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
}
