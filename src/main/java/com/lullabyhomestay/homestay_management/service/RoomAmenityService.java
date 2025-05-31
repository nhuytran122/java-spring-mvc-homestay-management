package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.RoomAmenity;
import com.lullabyhomestay.homestay_management.domain.id.RoomAmenityId;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.RoomAmenityRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RoomAmenityService {
    private final RoomAmenityRepository roomAmenityRepository;

    public List<RoomAmenity> getListRoomAmenitiesByRoomId(long roomId) {
        return this.roomAmenityRepository.findByRoom_RoomId(roomId);
    }

    public RoomAmenity handleSaveRoomAmenity(RoomAmenity roomAmenity) {
        return this.roomAmenityRepository.save(roomAmenity);
    }

    public RoomAmenity getRoomAmenityById(long roomId, long amenityId) {
        RoomAmenityId id = new RoomAmenityId(roomId, amenityId);
        Optional<RoomAmenity> roomAmenityOpt = roomAmenityRepository.findByRoomAmenityId(id);
        if (!roomAmenityOpt.isPresent()) {
            throw new NotFoundException("Tiện nghi phòng");
        }
        return roomAmenityOpt.get();
    }

    @Transactional
    public void deleteByRoomAmenityId(long roomId, long amenityId) {
        RoomAmenityId id = new RoomAmenityId(roomId, amenityId);
        roomAmenityRepository.deleteByRoomAmenityId(id);
    }

}
