package com.lullabyhomestay.homestay_management.controller.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.BookingServices;
import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.domain.Service;
import com.lullabyhomestay.homestay_management.domain.dto.ApiResponseDTO;
import com.lullabyhomestay.homestay_management.domain.dto.BookingServiceRequestDTO;
import com.lullabyhomestay.homestay_management.service.*;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class ClientBookingController {

    private final BookingExtraService bookingExtraService;
    private final BookingService bookingService;
    private final RoomService roomService;
    private final RoomStatusHistoryService roomStatusHistoryService;
    private final HomestayServiceService service;

    @PostMapping("/booking")
    public String postCreateBooking(@ModelAttribute("newBooking") @Valid Booking booking,
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

        redirectAttributes.addFlashAttribute("bookingID", booking.getBookingID());
        return "redirect:/booking/booking-service";
    }

    @GetMapping("/booking/booking-service")
    public String selectService(Model model, @ModelAttribute("bookingID") Long bookingID) {
        if (bookingID == null) {
            return "redirect:/";
        }
        Booking booking = bookingService.getBookingByID(bookingID);
        // TODO: thay bằng user đang login
        if (booking == null || !booking.getCustomer().getEmail().equals("nhuytran@gmail.com")) {
            return "redirect:/error";
        }
        model.addAttribute("bookingID", bookingID);
        model.addAttribute("listServices", service.getServiceByIsPrepaid(true));
        model.addAttribute("listNotPrePaidServices", service.getServiceByIsPrepaid(false));
        return "client/booking/booking-service";
    }

    @PostMapping("/booking/confirm-services")
    @ResponseBody
    public ResponseEntity<ApiResponseDTO<Long>> postConfirmBookingService(@RequestBody BookingServiceRequestDTO request,
            Model model,
            RedirectAttributes redirectAttributes) {
        Long bookingID = request.getBookingID();
        List<BookingServices> listBookingServices = request.getServices();
        Booking booking = bookingService.getBookingByID(bookingID);
        // TODO: thay bằng user đang login
        if (booking == null || !booking.getCustomer().getEmail().equals("nhuytran@gmail.com")) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO<>(null, "Không tìm thấy booking hoặc không đủ quyền truy cập booking"));
        }
        // model.addAttribute("booking", booking);
        if (listBookingServices != null && !listBookingServices.isEmpty()) {
            for (BookingServices bService : listBookingServices) {
                BookingServices newBookingService = new BookingServices();

                Long serviceID = bService.getService().getServiceID();
                Service service = this.service.getServiceByID(serviceID);
                Float quantity = bService.getQuantity();
                String description = bService.getDescription();
                Double totalPrice = service.getPrice() * quantity;

                newBookingService.setService(this.service.getServiceByID(serviceID));
                newBookingService.setQuantity(quantity);
                newBookingService.setTotalPrice(totalPrice);
                newBookingService.setIsAdditional(false);
                newBookingService.setDescription(description);
                newBookingService.setBooking(booking);

                this.bookingExtraService.handleSaveBookingServiceExtra(newBookingService);
            }
        }
        redirectAttributes.addFlashAttribute("bookingID", bookingID);
        return ResponseEntity.ok(new ApiResponseDTO<>(bookingID, "Xác nhận dịch vụ thành công"));
        // return ResponseEntity.ok().build();
        // return "client/booking/booking-confirmation";
    }

    @GetMapping("/booking/booking-confirmation")
    public String getBookingSuccessPage(@RequestParam("bookingID") Long bookingID, Model model) {
        Booking booking = bookingService.getBookingByID(bookingID);
        // TODO: thay bằng user đang login
        if (booking == null || !booking.getCustomer().getEmail().equals("nhuytran@gmail.com")) {
            return "redirect:/error";
        }
        model.addAttribute("booking", booking);
        return "client/booking/booking-confirmation";
    }

}