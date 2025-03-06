package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.RoomAmenity;
import com.lullabyhomestay.homestay_management.domain.id.RoomAmenityID;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.RoomAmenityRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RoomAmenityService {
    private final RoomAmenityRepository roomAmenityRepository;

    public List<RoomAmenity> getListRoomAmenitiesByRoomID(long roomID) {
        return this.roomAmenityRepository.findByRoom_RoomID(roomID);
    }

    public RoomAmenity handleSaveRoomAmenity(RoomAmenity roomAmenity) {
        return this.roomAmenityRepository.save(roomAmenity);
    }

    public RoomAmenity getRoomAmenityByID(long roomID, long amenityID) {
        RoomAmenityID id = new RoomAmenityID(roomID, amenityID);
        Optional<RoomAmenity> roomAmenityOpt = roomAmenityRepository.findByRoomAmenityID(id);
        if (!roomAmenityOpt.isPresent()) {
            throw new NotFoundException("Tiện nghi phòng");
        }
        return roomAmenityOpt.get();
    }

    @Transactional
    public void deleteByRoomAmenityID(long roomID, long amenityID) {
        RoomAmenityID id = new RoomAmenityID(roomID, amenityID);
        roomAmenityRepository.deleteByRoomAmenityID(id);
    }

}
