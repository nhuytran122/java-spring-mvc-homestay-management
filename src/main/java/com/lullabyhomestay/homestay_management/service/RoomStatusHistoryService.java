package com.lullabyhomestay.homestay_management.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.BookingExtension;
import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.domain.RoomStatusHistory;
import com.lullabyhomestay.homestay_management.domain.dto.BookingScheduleData;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.RoomStatusHistoryRepository;
import com.lullabyhomestay.homestay_management.utils.Constants;
import com.lullabyhomestay.homestay_management.utils.RoomStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RoomStatusHistoryService {

    private final RoomStatusHistoryRepository roomStatusHistoryRepo;
    private final RoomService roomService;

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

        // Tự động thêm CLEANING (1H sau khi checkout)
        LocalDateTime cleaningStartLocal = booking.getCheckOut();
        LocalDateTime cleaningEndLocal = cleaningStartLocal.plusHours(Constants.CLEANING_HOURS);
        RoomStatusHistory cleaningStatus = new RoomStatusHistory();
        cleaningStatus.setRoom(booking.getRoom());
        cleaningStatus.setStatus(RoomStatus.CLEANING);
        cleaningStatus.setStartedAt(cleaningStartLocal);
        cleaningStatus.setEndedAt(cleaningEndLocal);
        cleaningStatus.setBooking(booking);
        roomStatusHistoryRepo.save(cleaningStatus);
    }

    public void handleBookingExtensions(BookingExtension bookingExtension) {
        Booking booking = bookingExtension.getBooking();
        LocalDateTime newCheckout = booking.getCheckOut();

        // Tìm BUSY hiện tại
        RoomStatusHistory busyStatus = getScheduleByBookingIDAndRoomStatusHistories(
                booking.getBookingID(), RoomStatus.BUSY);
        busyStatus.setEndedAt(newCheckout);
        roomStatusHistoryRepo.save(busyStatus);

        // Tìm + set lại CLEANING
        RoomStatusHistory cleanStatus = getScheduleByBookingIDAndRoomStatusHistories(booking.getBookingID(),
                RoomStatus.CLEANING);
        cleanStatus.setStartedAt(newCheckout);
        cleanStatus.setEndedAt(newCheckout.plusHours(Constants.CLEANING_HOURS));
        roomStatusHistoryRepo.save(cleanStatus);
    }

    public List<RoomStatusHistory> getScheduleRoomByRoomIDAndDate(Long roomID,
            LocalDateTime date) {
        LocalDateTime startOfDate = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDate = date.toLocalDate().atTime(23, 59, 59);
        return roomStatusHistoryRepo.findByRoom_RoomIDAndStartedAtBetween(roomID,
                startOfDate, endOfDate);
    }

    public BookingScheduleData getBookingScheduleData(String dateStr, Long branchId) {
        LocalDateTime date;
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (dateStr != null && !dateStr.isEmpty()) {
            LocalDate localDate = LocalDate.parse(dateStr, inputFormatter);
            date = localDate.atStartOfDay();
        } else {
            date = LocalDateTime.now();
        }

        List<Room> rooms;
        if (branchId != null) {
            rooms = roomService.getRoomsByBranchID(branchId);
        } else {
            rooms = roomService.getAllRooms();
        }

        Map<Long, List<RoomStatusHistory>> roomSchedules = new HashMap<>();
        for (Room room : rooms) {
            List<RoomStatusHistory> schedule = getScheduleRoomByRoomIDAndDate(
                    room.getRoomID(), date);
            if (schedule != null && !schedule.isEmpty()) {
                roomSchedules.put(room.getRoomID(), schedule);
            }
        }

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateFormatted = date.toLocalDate().format(outputFormatter);

        return new BookingScheduleData(rooms, roomSchedules, date.toLocalDate(), dateFormatted);
    }

    public void deleteByBookingID(Long bookingID) {
        roomStatusHistoryRepo.deleteByBooking_BookingID(bookingID);
    }

    public RoomStatusHistory getScheduleByBookingIDAndRoomStatusHistories(Long bookingID,
            RoomStatus roomStatus) {
        Optional<RoomStatusHistory> roomHistory = roomStatusHistoryRepo.findByBooking_BookingIDAndStatus(bookingID,
                roomStatus);
        if (!roomHistory.isPresent()) {
            throw new NotFoundException("Lịch sử phòng");
        }
        return roomHistory.get();
    }
}
