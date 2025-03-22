package com.lullabyhomestay.homestay_management.controller.admin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.dto.BookingScheduleData;
import com.lullabyhomestay.homestay_management.domain.dto.SearchBookingCriteriaDTO;
import com.lullabyhomestay.homestay_management.service.BookingService;
import com.lullabyhomestay.homestay_management.service.BranchService;
import com.lullabyhomestay.homestay_management.service.RoomStatusHistoryService;
import com.lullabyhomestay.homestay_management.service.RoomTypeService;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class BookingController {
    private final BookingService bookingService;
    private final BranchService branchService;
    private final RoomTypeService roomTypeService;
    private final RoomStatusHistoryService roomStatusHistoryService;

    @GetMapping("/admin/booking")
    public String getBookingPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @ModelAttribute SearchBookingCriteriaDTO criteria) {
        int validPage = Math.max(1, page);
        String sort = (criteria.getSort() != null && !criteria.getSort().isEmpty()) ? criteria.getSort() : "desc";
        criteria.setSort(sort);

        if (criteria.getTimeRange() == null || criteria.getTimeRange().isEmpty()) {
            LocalDateTime startDefault = LocalDateTime.now();
            LocalDateTime endDefault = LocalDateTime.now().plusMonths(2);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            criteria.setTimeRange(startDefault.format(formatter) + " - " + endDefault.format(formatter));
        }
        if (criteria.getFromTime().isAfter(criteria.getToTime())) {
            model.addAttribute("errorMessage", "Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc!");
            return prepareModelWithoutSearch(model, criteria, validPage);
        }

        Page<Booking> bookings = bookingService.searchBookings(criteria, validPage);
        List<Booking> listBookings = bookings.getContent();
        model.addAttribute("totalPages", bookings.getTotalPages());
        model.addAttribute("listBookings", listBookings);
        return prepareModelWithoutSearch(model, criteria, validPage);
    }

    @GetMapping("/admin/booking/{id}")
    public String getDetailBookingPage(Model model, @PathVariable long id) {
        Booking booking = bookingService.getBookingByID(id);
        model.addAttribute("booking", booking);
        return "admin/booking/detail";
    }

    @GetMapping("/admin/booking/booking-history/can-cancel/{id}")
    public ResponseEntity<Boolean> canCancelBooking(@PathVariable Long id,
            HttpServletRequest request) {
        boolean canCancel = bookingService.canCancelBooking(id);
        return ResponseEntity.ok(canCancel);
    }

    @PostMapping("/admin/booking/booking-history/cancel")
    public String postCancelBooking(@RequestParam("bookingID") long bookingID,
            HttpServletRequest request) {
        this.bookingService.cancelBooking(bookingID);
        return "redirect:/booking/{bookingID}";
    }

    @GetMapping("/admin/booking/schedule")
    public String getBookingSchedulePage(
            Model model,
            @RequestParam(value = "date", required = false) String dateStr,
            @RequestParam(value = "branchID", required = false) Long branchID) {

        BookingScheduleData scheduleData = roomStatusHistoryService.getBookingScheduleData(dateStr, branchID);

        model.addAttribute("listBranches", branchService.getAllBranches());

        model.addAttribute("listRooms", scheduleData.getRooms());
        model.addAttribute("roomSchedules", scheduleData.getRoomSchedules());
        model.addAttribute("date", scheduleData.getDate());
        model.addAttribute("dateFormatted", scheduleData.getDateFormatted());
        model.addAttribute("branchID", branchID);
        return "admin/booking/booking-schedule";
    }

    private String prepareModelWithoutSearch(Model model, SearchBookingCriteriaDTO criteria, int validPage) {
        model.addAttribute("criteria", criteria);
        model.addAttribute("extraParams", criteria.convertToExtraParams());
        model.addAttribute("currentPage", validPage);
        model.addAttribute("bookingStatuses", BookingStatus.values());
        model.addAttribute("listBranches", this.branchService.getAllBranches());
        model.addAttribute("listRoomTypes", this.roomTypeService.getAllRoomTypes());
        return "admin/booking/show";
    }
}
