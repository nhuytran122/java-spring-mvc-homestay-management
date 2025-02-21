package com.lullabyhomestay.homestay_management.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.repository.RoomRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public Page<Room> getAllRooms(Pageable pageable) {
        return this.roomRepository.findAll(pageable);
    }
}
