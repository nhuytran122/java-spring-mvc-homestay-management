package com.lullabyhomestay.homestay_management.controller.client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.lullabyhomestay.homestay_management.domain.BookingExtension;
import com.lullabyhomestay.homestay_management.domain.BookingServices;
import com.lullabyhomestay.homestay_management.domain.Review;
import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.domain.RoomPricing;
import com.lullabyhomestay.homestay_management.domain.dto.ApiResponseDTO;
import com.lullabyhomestay.homestay_management.domain.dto.BookingPriceDTO;
import com.lullabyhomestay.homestay_management.domain.dto.BookingRequestDTO;
import com.lullabyhomestay.homestay_management.domain.dto.BookingServiceDTO;
import com.lullabyhomestay.homestay_management.domain.dto.CustomerDTO;
import com.lullabyhomestay.homestay_management.domain.dto.SearchBookingCriteriaDTO;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.service.*;
import com.lullabyhomestay.homestay_management.utils.AuthUtils;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;
import com.lullabyhomestay.homestay_management.utils.BookingUtils;
import com.lullabyhomestay.homestay_management.utils.Cancelability;
import com.lullabyhomestay.homestay_management.utils.Constants;
import com.lullabyhomestay.homestay_management.utils.RefundType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
// @PreAuthorize("hasRole('CUSTOMER')")
public class ClientBookingController {

    private final BookingExtraService bookingExtraService;
    private final BookingService bookingService;
    private final RoomService roomService;
    private final RoomStatusHistoryService roomStatusHistoryService;
    private final HomestayServiceService service;
    private final BranchService branchService;
    private final RoomTypeService roomTypeService;
    private final ModelMapper mapper;
    private final ReviewService reviewService;
    private final RefundService refundService;
    private final BookingExtensionService bookingExtensionService;
    private final UserService userService;
    private final RoomPricingService roomPricingService;

    @PostMapping("/booking")
    public String postCreateBooking(@ModelAttribute("newBooking") @Valid Booking booking,
            BindingResult result,
            Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Long roomID = booking.getRoom().getRoomID();
        Room room = roomService.getRoomByID(booking.getRoom().getRoomID());
        if (booking.getCheckIn() != null && !booking.getCheckIn().isAfter(LocalDateTime.now())) {
            result.rejectValue("checkIn", "error.newBooking", "Giờ check-in phải từ thời điểm hiện tại trở đi");
        }
        if (result.hasErrors()) {
            Optional<RoomPricing> roomPricingOpt = roomPricingService
                    .getDefaultRoomPricing(room.getRoomType().getRoomTypeID());
            if (!roomPricingOpt.isPresent()) {
                throw new NotFoundException("Giá phòng");
            }
            model.addAttribute("roomPricing", roomPricingOpt.get());
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
        booking.setStatus(BookingStatus.PENDING);
        booking.setRoom(room);
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(userService, mapper);
        BookingUtils.mapAndSetCustomerToBooking(booking, customerDTO, mapper);

        HttpSession session = request.getSession(false);
        BookingRequestDTO bookingRequest = new BookingRequestDTO();

        bookingRequest.setCheckin(booking.getCheckIn());
        bookingRequest.setCheckout(booking.getCheckOut());
        bookingRequest.setGuestCount(booking.getGuestCount());
        bookingRequest.setCustomerID(booking.getCustomer().getCustomerID());
        bookingRequest.setRoomID(roomID);
        session.setAttribute("bookingRequest", bookingRequest);
        // booking = bookingService.handleBooking(booking);

        // redirectAttributes.addFlashAttribute("bookingID", booking.getBookingID());
        return "redirect:/booking/booking-service";
    }

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

    @GetMapping("/booking/booking-service")
    public String selectService(Model model,
            HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        BookingRequestDTO bookingRequest = (BookingRequestDTO) session.getAttribute("bookingRequest");
        if (bookingRequest == null)
            return "redirect:/";
        model.addAttribute("listServices", service.getServiceByIsPrepaid(true));
        model.addAttribute("listNotPrePaidServices", service.getServiceByIsPrepaid(false));
        return "client/booking/booking-service";
    }

    @PostMapping("/booking/confirm-services")
    @ResponseBody
    public ResponseEntity<ApiResponseDTO<Long>> postConfirmBookingService(
            @RequestBody BookingRequestDTO requestServiceDTO,
            Model model, HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        BookingRequestDTO bookingRequest = (BookingRequestDTO) session.getAttribute("bookingRequest");
        if (bookingRequest == null)
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO<>(null, "Không tìm thấy booking"));
        Booking booking = new Booking();
        if (bookingRequest.isCreatedFlag()) {
            booking = bookingService.getBookingByID(bookingRequest.getBookingID());
            bookingExtraService.deleteByBookingID(bookingRequest.getBookingID());
        } else {
            bookingRequest.setCreatedFlag(true);
            booking.setCheckIn(bookingRequest.getCheckin());
            booking.setCheckOut(bookingRequest.getCheckout());
            booking.setGuestCount(bookingRequest.getGuestCount());
            booking.setRoom(roomService.getRoomByID(bookingRequest.getRoomID()));
            booking.setStatus(BookingStatus.PENDING);
            CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(userService, mapper);
            BookingUtils.mapAndSetCustomerToBooking(booking, customerDTO, mapper);
            booking = bookingService.handleBooking(booking);
        }
        List<BookingServiceDTO> listBookingServices = requestServiceDTO.getServices();
        if (listBookingServices != null && !listBookingServices.isEmpty()) {
            for (BookingServiceDTO bService : listBookingServices) {
                BookingServices newBookingService = new BookingServices();

                Long serviceID = bService.getServiceID();
                Float quantity = bService.getQuantity();
                String description = bService.getDescription();

                newBookingService.setService(this.service.getServiceByID(serviceID));
                newBookingService.setQuantity(quantity);

                newBookingService.setDescription(description);
                newBookingService.setBooking(booking);
                this.bookingExtraService.handleSaveBookingServiceExtra(newBookingService);
            }
        }
        bookingRequest.setBookingID(booking.getBookingID());
        bookingRequest.setServices(listBookingServices);
        session.setAttribute("bookingRequest", bookingRequest);
        // redirectAttributes.addAttribute("bookingID", booking.getBookingID());
        return ResponseEntity.ok(new ApiResponseDTO<>(booking.getBookingID(), "Xác nhận dịch vụ thành công"));
    }

    @GetMapping("/booking/booking-confirmation")
    public String getBookingConfirmationPage(
            @RequestParam("bookingID") Long bookingID,
            HttpServletRequest request,
            Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("bookingRequest") == null) {
            return "redirect:/";
        }
        BookingRequestDTO sessionDTO = (BookingRequestDTO) session.getAttribute("bookingRequest");

        if (!bookingID.equals(sessionDTO.getBookingID())) {
            return "redirect:/";
        }

        Booking booking = bookingService.getBookingByID(bookingID);
        model.addAttribute("booking", booking);
        double originalAmount = booking.getTotalAmount()
                / (1 - booking.getCustomer().getCustomerType().getDiscountRate() / 100);
        double discountAmount = originalAmount * (booking.getCustomer().getCustomerType().getDiscountRate() / 100);
        model.addAttribute("discountAmount", discountAmount);
        return "client/booking/booking-confirmation";
    }

    @GetMapping("/booking/booking-history")
    public String getBookingHistory(Model model,
            @RequestParam(defaultValue = "1") int page,
            @ModelAttribute SearchBookingCriteriaDTO criteria) {
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(userService, mapper);
        if (customerDTO == null) {
            throw new AccessDeniedException("Vui lòng đăng nhập để sử dụng chức năng này");
        }
        int validPage = Math.max(1, page);

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
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(userService, mapper);
        BookingUtils.validateBooking(booking, customerDTO);
        BookingUtils.mapAndSetCustomerToBooking(booking, customerDTO, mapper);

        model.addAttribute("canCancel", bookingService.checkCancelability(id) == Cancelability.ALLOWED);
        model.addAttribute("booking", booking);
        model.addAttribute("newReview", new Review());
        model.addAttribute("editReview", new Review());
        model.addAttribute("hasPrepaidBService", bookingExtraService.hasPrepaidService(id));
        model.addAttribute("hasPostpaidBService", bookingExtraService.hasPostpaidService(id));
        model.addAttribute("listServicesPostPay", service.getServiceByIsPrepaid(false));
        model.addAttribute("totalUnpaidPostpaidAmount", bookingExtraService.calculateUnpaidServicesTotalAmount(id));
        model.addAttribute("canPayBServices", bookingExtraService.allPostpaidServicesHaveQuantity(id));
        model.addAttribute("paidRoomPricing", 0);
        return "client/booking/detail-booking-history";
    }

    @GetMapping("/booking/check-refund/{bookingID}")
    public ResponseEntity<?> checkRefundForBooking(@PathVariable Long bookingID) {
        Booking booking = bookingService.getBookingByID(bookingID);
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(userService, mapper);
        BookingUtils.validateBooking(booking, customerDTO);

        Cancelability result = bookingService.checkCancelability(bookingID);

        switch (result) {
            case CANCELLED:
                return ResponseEntity.ok(new ApiResponseDTO<>(null, "Đơn đặt phòng đã bị hủy trước đó."));
            case COMPLETED:
                return ResponseEntity.ok(new ApiResponseDTO<>(null, "Đơn đặt phòng đã hoàn tất và không thể hủy."));
            case CHECKIN_TIME_PASSED:
                return ResponseEntity.ok(new ApiResponseDTO<>(null, "Thời gian nhận phòng đã đến, không thể hủy đơn."));
        }
        Map<String, Object> response = new HashMap<>();
        if (booking.getPaidAmount() == null || booking.getPaidAmount() <= 0) {
            response.put("refundAmount", 0.0);
            response.put("refundPercentage", 0);
            return ResponseEntity.ok(new ApiResponseDTO<>(response, "Không có khoản thanh toán nào để hoàn tiền"));
        }

        Double refundAmount = refundService.calculateRefundAmount(booking);
        RefundType refundType = refundService.getRefundType(booking);

        response.put("refundAmount", refundAmount);
        if (refundType == RefundType.FULL) {
            response.put("refundPercentage", 100);
        } else if (refundType == RefundType.PARTIAL_70) {
            response.put("refundPercentage", 70);
        } else {
            response.put("refundPercentage", 30);
        }

        return ResponseEntity.ok(new ApiResponseDTO<>(response, "Tính toán hoàn tiền thành công"));
    }

    @PostMapping("/booking/cancel")
    public String postCancelBooking(@RequestParam("bookingID") long bookingID) {
        Booking booking = bookingService.getBookingByID(bookingID);
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(userService, mapper);
        BookingUtils.validateBooking(booking, customerDTO);
        this.bookingService.cancelBooking(bookingID);
        return "redirect:/booking/booking-history";
    }

    @PostMapping("/booking/booking-service/create")
    public ResponseEntity<?> postCreateBookingServices(
            @RequestBody List<BookingServices> bookingServices) {
        for (BookingServices request : bookingServices) {
            Long bookingID = request.getBooking().getBookingID();
            Long serviceID = request.getService().getServiceID();

            Booking booking = bookingService.getBookingByID(bookingID);
            if (booking == null) {
                return ResponseEntity.ok().body(new ApiResponseDTO<>(false, "Không tìm thấy đơn đặt phòng."));
            }

            if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.COMPLETED) {
                return ResponseEntity.ok()
                        .body(new ApiResponseDTO<>(false, "Không thể đặt dịch vụ cho đơn đã hủy hoặc hoàn tất."));
            }

            BookingServices newBookingService = new BookingServices();
            newBookingService.setBooking(booking);
            newBookingService.setService(service.getServiceByID(serviceID));
            newBookingService.setDescription(request.getDescription());

            bookingExtraService.handleSaveBookingServiceExtra(newBookingService);
        }
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Đặt dịch vụ thành công."));
    }

    @GetMapping("/booking/can-booking-extension/{id}")
    public ResponseEntity<?> canBookingExtension(@PathVariable long id) {
        Booking booking = bookingService.getBookingByID(id);
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(userService, mapper);
        BookingUtils.validateBooking(booking, customerDTO);
        if (booking == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO<>(false, "Không tìm thấy đơn đặt phòng"));
        }
        if (booking.getStatus() == BookingStatus.PENDING) {
            return ResponseEntity
                    .ok(new ApiResponseDTO<>(false, "Vui lòng thanh toán đơn đặt phòng trước để dùng chức năng này"));
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return ResponseEntity.ok(new ApiResponseDTO<>(false, "Đơn đặt phòng đã bị hủy, không thể gia hạn"));
        }
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            return ResponseEntity.ok(new ApiResponseDTO<>(false, "Đơn đặt phòng đã hoàn tất, không thể gia hạn"));
        }
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Có thể gia hạn"));
    }

    @GetMapping("/booking/booking-extension/{id}")
    public String getBookingExtensionPage(@PathVariable long id, Model model) {
        Booking booking = bookingService.getBookingByID(id);
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(userService, mapper);
        BookingUtils.validateBooking(booking, customerDTO);
        model.addAttribute("booking", booking);
        return "client/booking/booking-extension";
    }

    @PostMapping("/booking/booking-extension/create")
    public String postBookingExtension(@RequestParam("bookingID") Long bookingID,
            @RequestParam("newCheckoutTime") @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime newCheckout,
            Model model) {
        Booking booking = bookingService.getBookingByID(bookingID);
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(userService, mapper);
        BookingUtils.validateBooking(booking, customerDTO);

        LocalDateTime currentCheckout = booking.getCheckOut();
        LocalDateTime checkOutWithCleaningBuffer = currentCheckout.plus(Constants.CLEANING_HOURS, ChronoUnit.HOURS);

        // Thời gian dọn dẹp sau check-out mới
        LocalDateTime newCheckoutWithCleaningBuffer = newCheckout.plus(Constants.CLEANING_HOURS, ChronoUnit.HOURS);

        // Kiểm tra trùng lịch từ checkOutWithCleaningBuffer đến
        // newCheckoutWithCleaningBuffer
        boolean isOverlapping = roomStatusHistoryService.existsOverlappingStatuses(
                booking.getRoom().getRoomID(),
                checkOutWithCleaningBuffer,
                newCheckoutWithCleaningBuffer);
        if (isOverlapping) {
            model.addAttribute("errorMessage", "Hiện thời gian bạn chọn đã có lịch trình");
            model.addAttribute("booking", booking);
            return "client/booking/booking-extension";
        }

        Float minutesDelay = (float) ChronoUnit.MINUTES.between(currentCheckout, newCheckout);
        Float hoursDelay = (float) (Math.ceil(minutesDelay / 30.0) * 0.5);
        if (hoursDelay <= 0) {
            model.addAttribute("errorMessage",
                    "Thời gian gia hạn không hợp lệ. Vui lòng chọn thời gian checkout mới sau thời gian hiện tại.");
            model.addAttribute("booking", booking);
            return "client/booking/booking-extension";
        }
        BookingExtension extension = new BookingExtension();
        extension.setBooking(booking);
        extension.setExtendedHours(hoursDelay);

        model.addAttribute("hoursDelay", hoursDelay);
        model.addAttribute("newCheckout", newCheckout);
        bookingExtensionService.handleBookingExtensions(extension);
        model.addAttribute("extension", extension);
        model.addAttribute("finalAmount", bookingExtensionService.calculateFinalExtensionAmount(extension));

        double originalAmount = bookingExtensionService.calculateRawTotalAmountBookingExtension(extension);
        double discountAmount = originalAmount * (booking.getCustomer().getCustomerType().getDiscountRate() / 100);
        model.addAttribute("discountAmount", discountAmount);
        return "client/booking/confirm-extension";
    }

    @PostMapping("/booking/booking-extension/cancel")
    public String postCancelBookingExtension(@RequestParam("id") Long id, Model model) {
        Booking booking = bookingService
                .getBookingByID(bookingExtensionService.getBookingExtensionByID(id).getBooking().getBookingID());
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(userService, mapper);
        BookingUtils.validateBooking(booking, customerDTO);

        bookingExtensionService.deleteByExtensionID(id);
        return "redirect:/booking/booking-extension/" + booking.getBookingID();
    }

    private String prepareModelWithoutSearch(Model model, SearchBookingCriteriaDTO criteria, int validPage) {
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(userService, mapper);
        model.addAttribute("customer", customerDTO);
        addCriteriaAttributes(model, criteria, validPage);
        addBookingStatistics(model, customerDTO.getCustomerID());
        return "client/booking/booking-history";
    }

    private void addBookingStatistics(Model model, Long customerID) {
        model.addAttribute("countPending",
                bookingService.countByBookingStatusAndCustomerID(BookingStatus.PENDING, customerID));
        model.addAttribute("countConfirmed",
                bookingService.countByBookingStatusAndCustomerID(BookingStatus.CONFIRMED, customerID));
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