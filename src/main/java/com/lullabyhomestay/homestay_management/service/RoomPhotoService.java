package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.RoomPhoto;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.RoomPhotoRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RoomPhotoService {
    private final RoomPhotoRepository roomPhotoRepository;

    public List<RoomPhoto> getRoomPhotosByRoomID(long roomID) {
        return this.roomPhotoRepository.findByRoom_RoomID(roomID);
    }

    public RoomPhoto getPhotoByPhotoID(long photoID) {
        Optional<RoomPhoto> roomPhotoOpt = roomPhotoRepository.findByPhotoID(photoID);
        if (!roomPhotoOpt.isPresent()) {
            throw new NotFoundException("Ảnh phòng");
        }
        return roomPhotoOpt.get();
    }

    public RoomPhoto handleSaveRoomPhoto(RoomPhoto roomPhoto) {
        return this.roomPhotoRepository.save(roomPhoto);
    }

    @Transactional
    public void deleteByPhotoID(long photoID) {
        this.roomPhotoRepository.deleteByPhotoID(photoID);
    }
}
