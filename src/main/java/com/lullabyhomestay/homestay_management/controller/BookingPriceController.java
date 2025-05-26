package com.lullabyhomestay.homestay_management.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lullabyhomestay.homestay_management.domain.dto.ApiResponseDTO;
import com.lullabyhomestay.homestay_management.domain.dto.BookingPriceDTO;
import com.lullabyhomestay.homestay_management.service.BookingService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class BookingPriceController {
    private final BookingService bookingService;

    @GetMapping("/booking/calculate-price")
    @ResponseBody
    public ResponseEntity<ApiResponseDTO<BookingPriceDTO>> calculatePrice(
            @RequestParam Long roomTypeId,
            @RequestParam String checkIn,
            @RequestParam String checkOut) {

        LocalDateTime checkInTime = LocalDateTime.parse(checkIn);
        LocalDateTime checkOutTime = LocalDateTime.parse(checkOut);

        BookingPriceDTO price = bookingService.getRoomPriceDetail(roomTypeId, checkInTime, checkOutTime);

        return ResponseEntity.ok(new ApiResponseDTO<>(price, "Tính giá thành công"));
    }
}
