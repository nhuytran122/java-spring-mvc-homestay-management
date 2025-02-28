package com.lullabyhomestay.homestay_management.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.repository.BookingRepository;
import com.lullabyhomestay.homestay_management.repository.MaintenanceRequestRepository;
import com.lullabyhomestay.homestay_management.repository.RoomAmenityRepository;
import com.lullabyhomestay.homestay_management.repository.RoomPhotoRepository;
import com.lullabyhomestay.homestay_management.repository.RoomRepository;
import com.lullabyhomestay.homestay_management.repository.RoomStatusHistoryRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final MaintenanceRequestRepository maintenanceRequestRepository;
    private final BookingRepository bookingRepository;
    private final RoomAmenityRepository roomAmenityRepository;
    private final RoomStatusHistoryRepository roomStatusRepository;
    private final RoomPhotoRepository roomPhotoRepository;

    public Page<Room> getAllRooms(Pageable pageable) {
        return this.roomRepository.findAll(pageable);
    }

    // todo: search by branchID & roomTypeID & description
    // public Page<Room> searchRooms(long branchID, long roomTypeID, Pageable
    // pageable){
    // return
    // this.roomRepository.findByBranch_BranchIDAndRoomType_RoomTypeID(branchID,
    // roomTypeID, pageable);
    // }

    public void handleSaveRoom(Room room) {
        this.roomRepository.save(room);
    }

    public Optional<Room> getRoomByID(long roomID) {
        return this.roomRepository.findByRoomID(roomID);
    }

    public boolean canDeleteRoom(long roomID) {
        boolean hasMaintenanceRequest = maintenanceRequestRepository.existsByRoom_RoomID(roomID);
        boolean hasBookings = bookingRepository.existsByRoom_RoomID(roomID);
        return !(hasMaintenanceRequest || hasBookings);
    }

    @Transactional
    public void deleteByRoomID(long roomID) {
        if (canDeleteRoom(roomID)) {
            this.roomRepository.deleteByRoomID(roomID);
            this.roomAmenityRepository.deleteByRoom_RoomID(roomID);
            this.roomStatusRepository.deleteByRoom_RoomID(roomID);
            this.roomPhotoRepository.deleteByRoom_RoomID(roomID);
        }
    }
}
