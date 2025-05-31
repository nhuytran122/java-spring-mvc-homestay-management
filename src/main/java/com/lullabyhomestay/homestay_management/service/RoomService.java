package com.lullabyhomestay.homestay_management.service;

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
import com.lullabyhomestay.homestay_management.exception.CannotDeleteException;
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

    public List<Room> getAllRooms() {
        return this.roomRepository.findAll();
    }

    public Page<Room> searchRooms(SearchRoomCriteriaDTO criteria, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE);
        if (criteria.getRoomTypeId() == null && criteria.getBranchId() == null) {
            return roomRepository.findAll(pageable);
        }
        Specification<Room> spec = Specification.where(RoomSpecifications.hasBranch(criteria.getBranchId()))
                .and(RoomSpecifications.hasRoomType(criteria.getRoomTypeId()));
        return roomRepository.findAll(spec, pageable);
    }

    public Page<Room> searchRoomsForClient(SearchRoomCriteriaDTO criteria, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE);

        return roomRepository.findAvailableRooms(criteria.getBranchId(), criteria.getRoomTypeId(),
                pageable);
    }

    public Room handleSaveRoom(Room room) {
        return this.roomRepository.save(room);
    }

    public Room getRoomById(long roomId) {
        Optional<Room> roomOpt = this.roomRepository.findByRoomId(roomId);
        if (!roomOpt.isPresent()) {
            throw new NotFoundException("Phòng");
        }
        return roomOpt.get();
    }

    public List<Room> getRoomsByBranchId(long branchId) {
        return roomRepository.findByBranch_BranchId(branchId);
    }

    public List<RoomDTO> getRoomDTOsByBranchId(Long branchId) {
        List<Room> rooms = roomRepository.findByBranch_BranchId(branchId);
        List<RoomDTO> roomDTOs = new ArrayList<>();
        for (Room room : rooms) {
            RoomDTO roomDTO = new RoomDTO(room.getRoomId(), room.getRoomNumber());
            roomDTOs.add(roomDTO);
        }
        return roomDTOs;
    }

    public boolean canDeleteRoom(long roomId) {
        boolean hasMaintenanceRequest = maintenanceRequestRepository.existsByRoom_RoomId(roomId);
        boolean hasBookings = bookingRepository.existsByRoom_RoomId(roomId);
        return !(hasMaintenanceRequest || hasBookings);
    }

    @Transactional
    public void deleteByRoomId(long roomId) {
        if (!canDeleteRoom(roomId)) {
            throw new CannotDeleteException("Phòng");
        }
        this.roomRepository.deleteByRoomId(roomId);
        this.roomAmenityRepository.deleteByRoom_RoomId(roomId);
        this.roomStatusRepository.deleteByRoom_RoomId(roomId);
        this.roomPhotoRepository.deleteByRoom_RoomId(roomId);
    }
}
