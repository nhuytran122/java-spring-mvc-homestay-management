package com.lullabyhomestay.homestay_management.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.domain.dto.RoomDTO;
import com.lullabyhomestay.homestay_management.domain.dto.SearchRoomCriteriaDTO;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.BookingRepository;
import com.lullabyhomestay.homestay_management.repository.MaintenanceRequestRepository;
import com.lullabyhomestay.homestay_management.repository.RoomAmenityRepository;
import com.lullabyhomestay.homestay_management.repository.RoomPhotoRepository;
import com.lullabyhomestay.homestay_management.repository.RoomRepository;
import com.lullabyhomestay.homestay_management.repository.RoomStatusHistoryRepository;
import com.lullabyhomestay.homestay_management.service.specifications.RoomSpecifications;
import com.lullabyhomestay.homestay_management.utils.Constants;

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

    public Page<Room> searchRooms(SearchRoomCriteriaDTO criteria, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE);
        if (criteria.getRoomTypeID() == null && criteria.getBranchID() == null) {
            return roomRepository.findAll(pageable);
        }
        Specification<Room> spec = Specification.where(RoomSpecifications.hasBranch(criteria.getBranchID()))
                .and(RoomSpecifications.hasRoomType(criteria.getRoomTypeID()));
        return roomRepository.findAll(spec, pageable);
    }

    public Page<Room> searchRoomsForClient(SearchRoomCriteriaDTO criteria, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE);
        LocalDateTime startTime = criteria.getFromTime();
        LocalDateTime endTime = criteria.getToTime();

        if (startTime == null || endTime == null)
            throw new IllegalArgumentException("Thời gian checkin phải bé hơn thời gian checkout");

        return roomRepository.findAvailableRooms(criteria.getBranchID(), criteria.getRoomTypeID(), startTime, endTime,
                pageable);
    }

    public Room handleSaveRoom(Room room) {
        return this.roomRepository.save(room);
    }

    public Room getRoomByID(long roomID) {
        Optional<Room> roomOpt = this.roomRepository.findByRoomID(roomID);
        if (!roomOpt.isPresent()) {
            throw new NotFoundException("Phòng");
        }
        return roomOpt.get();
    }

    public List<Room> getRoomsByBranchID(long branchID) {
        return roomRepository.findByBranch_BranchID(branchID);
    }

    public List<RoomDTO> getRoomDTOsByBranchID(Long branchID) {
        List<Room> rooms = roomRepository.findByBranch_BranchID(branchID);
        List<RoomDTO> roomDTOs = new ArrayList<>();
        for (Room room : rooms) {
            RoomDTO roomDTO = new RoomDTO(room.getRoomID(), room.getRoomNumber());
            roomDTOs.add(roomDTO);
        }
        return roomDTOs;
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
