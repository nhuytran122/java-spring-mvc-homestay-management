package com.lullabyhomestay.homestay_management.controller.client;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.domain.RoomPricing;
import com.lullabyhomestay.homestay_management.domain.dto.BookingRequestDTO;
import com.lullabyhomestay.homestay_management.domain.dto.SearchRoomCriteriaDTO;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class ClientRoomController {

    private final BookingService bookingService;
    private final RoomService roomService;
    private final RoomTypeService roomTypeService;
    private final BranchService branchService;
    private final ReviewService reviewService;
    private final RoomPricingService roomPricingService;

    @GetMapping("/room")
    public String getRoomsPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @ModelAttribute SearchRoomCriteriaDTO criteria) {
        int validPage = Math.max(1, page);
        Page<Room> rooms = roomService.searchRoomsForClient(criteria, validPage);
        List<Room> listRooms = rooms.getContent();
        model.addAttribute("rooms", listRooms);
        model.addAttribute("totalPages", rooms.getTotalPages());
        return prepareModelWithoutSearch(model, criteria, validPage);
    }

    @GetMapping("/room/{id}")
    public String getDetailRoomPage(Model model,
            @PathVariable long id,
            HttpServletRequest request,
            @RequestParam(value = "fromBookingService", required = false) boolean fromBookingService) {
        Room room = roomService.getRoomById(id);
        Optional<RoomPricing> roomPricingOpt = roomPricingService
                .getDefaultRoomPricing(room.getRoomType().getRoomTypeId());
        if (!roomPricingOpt.isPresent()) {
            throw new NotFoundException("Giá phòng");
        }
        model.addAttribute("roomPricing", roomPricingOpt.get());
        model.addAttribute("newBooking", new Booking());
        model.addAttribute("room", room);
        model.addAttribute("roomPricing", roomPricingOpt.get());
        model.addAttribute("listReviews", reviewService.getReviewsByRoomId(id));

        // Đối với trường hợp user back từ confirm -> detail room
        HttpSession session = request.getSession(false);
        BookingRequestDTO bookingRequest = (BookingRequestDTO) session.getAttribute("bookingRequest");
        if (bookingRequest != null && bookingRequest.getBookingId() != null && fromBookingService) {
            bookingService.deleteByBookingId(bookingRequest.getBookingId());
            session.setAttribute("bookingRequest", null);
        }
        return "client/room/detail";
    }

    private String prepareModelWithoutSearch(Model model, SearchRoomCriteriaDTO criteria, int validPage) {
        model.addAttribute("criteria", criteria);
        model.addAttribute("extraParams", criteria.convertToExtraParams());
        model.addAttribute("currentPage", validPage);
        model.addAttribute("listBranches", this.branchService.getAllBranches());
        model.addAttribute("listRoomTypes", this.roomTypeService.getAllRoomTypes());
        return "client/room/show";
    }

}
