package com.lullabyhomestay.homestay_management.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.RoomStatusHistory;
import com.lullabyhomestay.homestay_management.repository.RoomStatusHistoryRepository;
import com.lullabyhomestay.homestay_management.utils.RoomStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RoomStatusHistoryService {

    private final RoomStatusHistoryRepository roomStatusHistoryRepo;

    public boolean existsOverlappingStatuses(Long roomID, LocalDateTime startedAt, LocalDateTime endedAt) {
        return roomStatusHistoryRepo.existsOverlappingStatuses(roomID, startedAt, endedAt);
    }

    public void handleStatusWhenBooking(Booking booking) {
        RoomStatusHistory busyStatus = new RoomStatusHistory();
        busyStatus.setRoom(booking.getRoom());
        busyStatus.setStatus(RoomStatus.BUSY);
        busyStatus.setStartedAt(booking.getCheckIn());
        busyStatus.setEndedAt(booking.getCheckOut());
        busyStatus.setBooking(booking);
        roomStatusHistoryRepo.save(busyStatus);

        // Tự động thêm CLEANING (40' sau khi checkout)
        LocalDateTime cleaningStartLocal = booking.getCheckOut();
        LocalDateTime cleaningEndLocal = cleaningStartLocal.plusMinutes(40);
        RoomStatusHistory cleaningStatus = new RoomStatusHistory();
        cleaningStatus.setRoom(booking.getRoom());
        cleaningStatus.setStatus(RoomStatus.CLEANING);
        cleaningStatus.setStartedAt(cleaningStartLocal);
        cleaningStatus.setEndedAt(cleaningEndLocal);
        cleaningStatus.setBooking(booking);
        roomStatusHistoryRepo.save(cleaningStatus);
    }
}
