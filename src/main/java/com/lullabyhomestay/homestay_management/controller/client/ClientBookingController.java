package com.lullabyhomestay.homestay_management.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.service.BookingService;
import com.lullabyhomestay.homestay_management.service.RoomService;
import com.lullabyhomestay.homestay_management.service.RoomStatusHistoryService;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class ClientBookingController {
    private final BookingService bookingService;;
    private final RoomService roomService;;
    private final RoomStatusHistoryService roomStatusHistoryService;

    @PostMapping("/booking")
    public String createBooking(@ModelAttribute("newBooking") @Valid Booking booking,
            BindingResult result,
            Model model, RedirectAttributes redirectAttributes) {
        Long roomID = booking.getRoom().getRoomID();
        Room room = roomService.getRoomByID(booking.getRoom().getRoomID());

        if (result.hasErrors()) {
            model.addAttribute("room", room);
            return "client/room/detail";
        }

        boolean hasOverlap = roomStatusHistoryService.existsOverlappingStatuses(roomID, booking.getCheckIn(),
                booking.getCheckOut());
        if (hasOverlap) {
            model.addAttribute("errorMessage",
                    "Phòng này đã có lịch trong thời gian bạn chọn, vui lòng chọn thời gian khác hoặc phòng khác");
            model.addAttribute("room", room);
            return "client/room/detail";
        }
        booking.setStatus(BookingStatus.BOOKED);
        booking.setRoom(room);
        booking = bookingService.handleBooking(booking);
        redirectAttributes.addFlashAttribute("booking", booking);
        return "redirect:/booking/booking-success";
    }

    @PostMapping("/booking/booking-success")
    public String getBookingSuccessPage(@ModelAttribute("bookingID") Long bookingID, Model model) {
        Booking booking = bookingService.getBookingByID(bookingID);
        // TODO: thay bằng user đang login
        if (booking == null || !booking.getCustomer().getEmail().equals("nhuytran@gmail.com")) {
            return "redirect:/error";
        }
        model.addAttribute("booking", booking);
        // return "client/booking/booking-success";
        return "redirect:/booking/booking-service";
    }

    

}