package com.lullabyhomestay.homestay_management.domain.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.domain.RoomStatusHistory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingScheduleData {
    private List<Room> rooms;
    private Map<Long, List<RoomStatusHistory>> roomSchedules;
    private LocalDate date;
    private String dateFormatted;
}