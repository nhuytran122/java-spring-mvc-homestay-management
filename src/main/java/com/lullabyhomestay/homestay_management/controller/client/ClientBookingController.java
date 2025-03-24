package com.lullabyhomestay.homestay_management.controller.client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.lullabyhomestay.homestay_management.domain.Review;
import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.domain.dto.ApiResponseDTO;
import com.lullabyhomestay.homestay_management.domain.dto.BookingServiceRequestDTO;
import com.lullabyhomestay.homestay_management.domain.dto.CustomerDTO;
import com.lullabyhomestay.homestay_management.domain.dto.SearchBookingCriteriaDTO;
import com.lullabyhomestay.homestay_management.service.*;
import com.lullabyhomestay.homestay_management.utils.AuthUtils;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;
import com.lullabyhomestay.homestay_management.utils.BookingUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class ClientBookingController {

    private final BookingExtraService bookingExtraService;
    private final BookingService bookingService;
    private final RoomService roomService;
    private final RoomStatusHistoryService roomStatusHistoryService;
    private final HomestayServiceService service;
    private final CustomerService customerService;
    private final BranchService branchService;
    private final RoomTypeService roomTypeService;
    private final ModelMapper mapper;
    private final ReviewService reviewService;

    @PostMapping("/booking")
    public String postCreateBooking(@ModelAttribute("newBooking") @Valid Booking booking,
            BindingResult result,
            Model model, RedirectAttributes redirectAttributes) {
        Long roomID = booking.getRoom().getRoomID();
        Room room = roomService.getRoomByID(booking.getRoom().getRoomID());

        if (result.hasErrors()) {
            model.addAttribute("room", room);
            model.addAttribute("listReviews", reviewService.getReviewsByRoomID(roomID));
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
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(customerService);
        BookingUtils.mapAndSetCustomerToBooking(booking, customerDTO, mapper);
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
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(customerService);
        BookingUtils.mapAndSetCustomerToBooking(booking, customerDTO, mapper);

        BookingUtils.validateBooking(booking, customerDTO);
        model.addAttribute("bookingID", bookingID);
        model.addAttribute("listServices", service.getServiceByIsPrepaid(true));
        model.addAttribute("listNotPrePaidServices", service.getServiceByIsPrepaid(false));
        return "client/booking/booking-service";
    }

    @PostMapping("/booking/confirm-services")
    @ResponseBody
    public ResponseEntity<ApiResponseDTO<Long>> postConfirmBookingService(
            @RequestBody BookingServiceRequestDTO requestDTO,
            Model model) {
        Long bookingID = requestDTO.getBookingID();
        List<BookingServices> listBookingServices = requestDTO.getServices();
        Booking booking = bookingService.getBookingByID(bookingID);

        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(customerService);
        BookingUtils.mapAndSetCustomerToBooking(booking, customerDTO, mapper);

        if (booking == null || !booking.getCustomer().getEmail().equals(customerDTO.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO<>(null, "Không tìm thấy booking hoặc không đủ quyền truy cập booking"));
        }
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
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(customerService);
        BookingUtils.validateBooking(booking, customerDTO);
        model.addAttribute("booking", booking);
        return "client/booking/booking-confirmation";
    }

    @GetMapping("/booking/booking-history")
    public String getBookingHistory(Model model,
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

        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(customerService);
        if (customerDTO == null) {
            throw new AccessDeniedException("Vui lòng đăng nhập để sử dụng chức năng này");
        }
        criteria.setCustomerID(customerDTO.getCustomerID());
        Page<Booking> bookings = bookingService.searchBookings(criteria, validPage);
        List<Booking> listBookings = bookings.getContent();
        model.addAttribute("totalPages", bookings.getTotalPages());
        model.addAttribute("listBookings", listBookings);
        return prepareModelWithoutSearch(model, criteria, validPage);
    }

    @GetMapping("/booking/booking-history/{id}")
    public String getDetailBooking(Model model, @PathVariable long id) {
        Booking booking = bookingService.getBookingByID(id);
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(customerService);
        BookingUtils.validateBooking(booking, customerDTO);
        BookingUtils.mapAndSetCustomerToBooking(booking, customerDTO, mapper);

        model.addAttribute("booking", booking);
        model.addAttribute("numberOfHours", booking.getNumberOfHours());
        model.addAttribute("newReview", new Review());
        model.addAttribute("editReview", new Review());
        return "client/booking/detail-booking-history";
    }

    @GetMapping("/booking/booking-history/can-cancel/{id}")
    public ResponseEntity<Boolean> canCancelBooking(@PathVariable Long id) {

        Booking booking = bookingService.getBookingByID(id);
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(customerService);
        BookingUtils.validateBooking(booking, customerDTO);
        boolean canCancel = bookingService.canCancelBooking(id);
        return ResponseEntity.ok(canCancel);
    }

    @PostMapping("/booking/booking-history/cancel")
    public String postCancelBooking(@RequestParam("bookingID") long bookingID) {
        Booking booking = bookingService.getBookingByID(bookingID);
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(customerService);
        BookingUtils.validateBooking(booking, customerDTO);
        this.bookingService.cancelBooking(bookingID);
        return "redirect:/booking/booking-history";
    }

    private String prepareModelWithoutSearch(Model model, SearchBookingCriteriaDTO criteria, int validPage) {
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(customerService);
        model.addAttribute("customer", customerDTO);
        addCriteriaAttributes(model, criteria, validPage);
        addBookingStatistics(model, customerDTO.getCustomerID());
        return "client/booking/booking-history";
    }

    private void addBookingStatistics(Model model, Long customerID) {
        model.addAttribute("countBooked",
                bookingService.countByBookingStatusAndCustomerID(BookingStatus.BOOKED, customerID));
        model.addAttribute("countCancelled",
                bookingService.countByBookingStatusAndCustomerID(BookingStatus.CANCELLED, customerID));
        model.addAttribute("countCompleted",
                bookingService.countByBookingStatusAndCustomerID(BookingStatus.COMPLETED, customerID));
        model.addAttribute("countTotal", bookingService.countTotalBookingByCustomerID(customerID));
    }

    private void addCriteriaAttributes(Model model, SearchBookingCriteriaDTO criteria, int validPage) {
        model.addAttribute("criteria", criteria);
        model.addAttribute("extraParams", criteria.convertToExtraParams());
        model.addAttribute("currentPage", validPage);
        model.addAttribute("bookingStatuses", BookingStatus.values());
        model.addAttribute("listBranches", branchService.getAllBranches());
        model.addAttribute("listRoomTypes", roomTypeService.getAllRoomTypes());
    }

}