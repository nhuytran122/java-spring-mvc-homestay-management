package com.lullabyhomestay.homestay_management.controller.client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.BookingServices;
import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.domain.dto.ApiResponseDTO;
import com.lullabyhomestay.homestay_management.domain.dto.BookingServiceRequestDTO;
import com.lullabyhomestay.homestay_management.domain.dto.SearchBookingCriteriaDTO;
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
    private final CustomerService customerService;
    private final BranchService branchService;
    private final RoomTypeService roomTypeService;

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

                Float quantity = bService.getQuantity();
                String description = bService.getDescription();

                newBookingService.setService(this.service.getServiceByID(serviceID));
                newBookingService.setQuantity(quantity);

                newBookingService.setDescription(description);
                newBookingService.setBooking(booking);

                this.bookingExtraService.handleSaveBookingServiceExtra(newBookingService);
            }
        }
        return ResponseEntity.ok(new ApiResponseDTO<>(bookingID, "Xác nhận dịch vụ thành công"));
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

    @GetMapping("/booking/booking-history")
    public String getMethodName(Model model,
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
        // todo: thay = customerID đang đăng nhập
        criteria.setCustomerID(4l);

        // TODO: Security
        if (criteria.getCustomerID() == null) {
            throw new AccessDeniedException("Không đủ quyền truy cập");
        }
        Page<Booking> bookings = bookingService.searchBookings(criteria, validPage);
        List<Booking> listBookings = bookings.getContent();
        model.addAttribute("totalPages", bookings.getTotalPages());
        model.addAttribute("listBookings", listBookings);
        return prepareModelWithoutSearch(model, criteria, validPage);
    }

    @GetMapping("/booking/booking-history/{id}")
    public String getDetailBooking(Model model, @PathVariable long id) {
        // TODO
        // if () {
        // throw new AccessDeniedException("Không đủ quyền truy cập");
        // }
        Booking booking = bookingService.getBookingByID(id);
        model.addAttribute("booking", booking);
        model.addAttribute("numberOfHours", booking.getNumberOfHours());
        return "client/booking/detail-booking-history";
    }

    @GetMapping("/booking/booking-history/can-cancel/{id}")
    public ResponseEntity<Boolean> canCancelBooking(@PathVariable Long id) {
        boolean canCancel = bookingService.canCancelBooking(id);
        return ResponseEntity.ok(canCancel);
    }

    @PostMapping("/booking/booking-history/cancel")
    public String postCancelBooking(@RequestParam("bookingID") long bookingID) {
        this.bookingService.cancelBooking(bookingID);
        return "redirect:/booking/booking-history";
    }

    private String prepareModelWithoutSearch(Model model, SearchBookingCriteriaDTO criteria, int validPage) {
        model.addAttribute("customer", customerService.getCustomerDTOByID(4L));
        model.addAttribute("criteria", criteria);
        model.addAttribute("extraParams", criteria.convertToExtraParams());
        model.addAttribute("currentPage", validPage);
        model.addAttribute("bookingStatuses", BookingStatus.values());
        model.addAttribute("listBranches", this.branchService.getAllBranches());
        model.addAttribute("listRoomTypes", this.roomTypeService.getAllRoomTypes());
        model.addAttribute("countBooked",
                this.bookingService.countByBookingStatusAndCustomerID(BookingStatus.BOOKED, 4L));
        model.addAttribute("countCancelled",
                this.bookingService.countByBookingStatusAndCustomerID(BookingStatus.CANCELLED, 4L));
        model.addAttribute("countCompleted",
                this.bookingService.countByBookingStatusAndCustomerID(BookingStatus.COMPLETED, 4L));
        model.addAttribute("countTotal",
                this.bookingService.countTotalBookingByCustomerID(4L));
        return "client/booking/booking-history";
    }

}