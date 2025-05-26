package com.lullabyhomestay.homestay_management.controller.admin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.BookingExtension;
import com.lullabyhomestay.homestay_management.domain.BookingServices;
import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.domain.dto.ApiResponseDTO;
import com.lullabyhomestay.homestay_management.domain.dto.BookingRequestDTO;
import com.lullabyhomestay.homestay_management.domain.dto.BookingScheduleData;
import com.lullabyhomestay.homestay_management.domain.dto.BookingServiceDTO;
import com.lullabyhomestay.homestay_management.domain.dto.PaymentConfirmationRequestDTO;
import com.lullabyhomestay.homestay_management.domain.dto.SearchBookingCriteriaDTO;
import com.lullabyhomestay.homestay_management.service.BookingExtensionService;
import com.lullabyhomestay.homestay_management.service.BookingExtraService;
import com.lullabyhomestay.homestay_management.service.BookingService;
import com.lullabyhomestay.homestay_management.service.BranchService;
import com.lullabyhomestay.homestay_management.service.CustomerService;
import com.lullabyhomestay.homestay_management.service.HomestayServiceService;
import com.lullabyhomestay.homestay_management.service.PaymentService;
import com.lullabyhomestay.homestay_management.service.RefundService;
import com.lullabyhomestay.homestay_management.service.RoomService;
import com.lullabyhomestay.homestay_management.service.RoomStatusHistoryService;
import com.lullabyhomestay.homestay_management.service.RoomTypeService;
import com.lullabyhomestay.homestay_management.service.validator.AdminValidation;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;
import com.lullabyhomestay.homestay_management.utils.Cancelability;
import com.lullabyhomestay.homestay_management.utils.Constants;
import com.lullabyhomestay.homestay_management.utils.PaymentPurpose;
import com.lullabyhomestay.homestay_management.utils.RefundType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RequestMapping("/admin/booking")
@Controller
public class BookingController {
    private final BookingService bookingService;
    private final BranchService branchService;
    private final RoomTypeService roomTypeService;
    private final RoomStatusHistoryService roomStatusHistoryService;
    private final BookingExtensionService bookingExtensionService;
    private final CustomerService customerService;
    private final RoomService roomService;
    private final HomestayServiceService service;
    private final BookingExtraService bookingExtraService;
    private final PaymentService paymentService;
    private final RefundService refundService;

    @GetMapping("")
    public String getBookingPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @ModelAttribute SearchBookingCriteriaDTO criteria, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        BookingRequestDTO bookingRequest = (BookingRequestDTO) session.getAttribute("bookingRequest");
        if (bookingRequest != null && bookingRequest.getBookingID() != null) {
            session.setAttribute("bookingRequest", null);
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

        Page<Booking> bookings = bookingService.searchBookings(criteria, validPage);
        List<Booking> listBookings = bookings
                .getContent();
        model.addAttribute("totalPages", bookings.getTotalPages());
        model.addAttribute("listBookings", listBookings);
        return

        prepareModelWithoutSearch(model, criteria, validPage);
    }

    @GetMapping("/{id}")
    public String getDetailBookingPage(Model model, @PathVariable long id) {
        Booking booking = bookingService.getBookingByID(id);
        model.addAttribute("booking", booking);

        model.addAttribute("totalUnpaidPostpaidAmount", bookingExtraService.calculateUnpaidServicesTotalAmount(id));
        model.addAttribute("canPayBServices", bookingExtraService.allPostpaidServicesHaveQuantity(id));
        return "admin/booking/detail";
    }

    @GetMapping("/create")
    public String getCreateBookingPage(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        BookingRequestDTO bookingRequest = (BookingRequestDTO) session.getAttribute("bookingRequest");
        Booking newBooking = new Booking();
        if (bookingRequest != null) {
            newBooking.setCheckIn(bookingRequest.getCheckin());
            newBooking.setCheckOut(bookingRequest.getCheckout());
            newBooking.setGuestCount(bookingRequest.getGuestCount());
            newBooking.setRoom(roomService.getRoomByID(bookingRequest.getRoomID()));
            newBooking.setCustomer(customerService.getCustomerByID(bookingRequest.getCustomerID()));
            if (bookingRequest.getBookingID() != null) {
                session.setAttribute("bookingRequest", null);
                bookingService.deleteByBookingID(bookingRequest.getBookingID());
            }
        }
        model.addAttribute("newBooking", newBooking);
        model.addAttribute("listCustomers", customerService.getAllCustomers());
        model.addAttribute("listRooms", roomService.getAllRooms());
        return "admin/booking/create";
    }

    @PostMapping("/create")
    public String postCreateBooking(
            @ModelAttribute("newBooking") @Validated(AdminValidation.class) @Valid Booking booking,
            BindingResult result,
            Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        if (booking.getCheckIn() != null && !booking.getCheckIn().isAfter(LocalDateTime.now())) {
            model.addAttribute("listCustomers", customerService.getAllCustomers());
            model.addAttribute("listRooms", roomService.getAllRooms());
            result.rejectValue("checkIn", "error.newBooking", "Giờ check-in phải từ thời điểm hiện tại trở đi");
        }
        if (result.hasErrors()) {
            model.addAttribute("listCustomers", customerService.getAllCustomers());
            model.addAttribute("listRooms", roomService.getAllRooms());
            return "admin/booking/create";
        }
        Long roomID = booking.getRoom().getRoomID();
        Room room = roomService.getRoomByID(booking.getRoom().getRoomID());
        boolean hasOverlap = roomStatusHistoryService.existsOverlappingStatuses(roomID, booking.getCheckIn(),
                booking.getCheckOut());
        if (hasOverlap) {
            model.addAttribute("errorMessage",
                    "Phòng này đã có lịch trong thời gian bạn chọn, vui lòng chọn thời gian khác hoặc phòng khác");
            model.addAttribute("listCustomers", customerService.getAllCustomers());
            model.addAttribute("listRooms", roomService.getAllRooms());
            return "admin/booking/create";
        }
        booking.setStatus(BookingStatus.PENDING);
        booking.setRoom(room);
        booking.setCustomer(customerService.getCustomerByID(booking.getCustomer().getCustomerID()));

        HttpSession session = request.getSession(false);
        BookingRequestDTO bookingRequest = new BookingRequestDTO();

        bookingRequest.setCheckin(booking.getCheckIn());
        bookingRequest.setCheckout(booking.getCheckOut());
        bookingRequest.setGuestCount(booking.getGuestCount());
        bookingRequest.setCustomerID(booking.getCustomer().getCustomerID());
        bookingRequest.setRoomID(roomID);
        session.setAttribute("bookingRequest", bookingRequest);

        return "redirect:/admin/booking/booking-service";
    }

    @GetMapping("/booking-service")
    public String selectService(Model model,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        BookingRequestDTO bookingRequest = (BookingRequestDTO) session.getAttribute("bookingRequest");
        if (bookingRequest == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Phiên làm việc đã hết. Vui lòng thực hiện lại.");
            return "redirect:/admin/booking";
        }
        model.addAttribute("listServices", service.getServiceByIsPrepaid(true));
        return "admin/booking/booking-service";
    }

    @PostMapping("/confirm-service")
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
            booking.setCustomer(customerService.getCustomerByID(bookingRequest.getCustomerID()));
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
        return ResponseEntity.ok(new ApiResponseDTO<>(booking.getBookingID(), "Xác nhận dịch vụ thành công"));
    }

    @GetMapping("/booking-confirmation")
    public String getBookingConfirmationPage(
            @RequestParam("bookingID") Long bookingID,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            Model model) {

        Booking booking = bookingService.getBookingByID(bookingID);

        if (booking.getStatus() != BookingStatus.PENDING) {
            redirectAttributes.addFlashAttribute("errorMessage", "Chỉ có thể xác nhận các đơn đang chờ thanh toán.");
            return "redirect:/admin/booking";
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("bookingRequest") == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Phiên làm việc đã hết. Vui lòng thực hiện lại.");
            return "redirect:/admin/booking";
        }

        BookingRequestDTO sessionDTO = (BookingRequestDTO) session.getAttribute("bookingRequest");

        if (!bookingID.equals(sessionDTO.getBookingID())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thông tin đặt phòng không hợp lệ.");
            return "redirect:/admin/booking";
        }

        model.addAttribute("booking", booking);

        double originalAmount = booking.getTotalAmount()
                / (1 - booking.getCustomer().getCustomerType().getDiscountRate() / 100);
        double discountAmount = originalAmount * (booking.getCustomer().getCustomerType().getDiscountRate() / 100);
        model.addAttribute("discountAmount", discountAmount);

        return "admin/booking/booking-confirmation";
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<ApiResponseDTO<Long>> confirmPayment(
            @RequestBody PaymentConfirmationRequestDTO request,
            HttpSession session) {
        try {
            BookingRequestDTO bookingRequest = (BookingRequestDTO) session.getAttribute("bookingRequest");
            Long bookingID = bookingRequest.getBookingID();
            if (bookingRequest == null || !bookingID.equals(request.getBookingID())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponseDTO<>(null, "Không tìm thấy booking"));
            }
            paymentService.handleSavePaymentWithAdmin(bookingID, request.getPaymentMethod(),
                    PaymentPurpose.ROOM_BOOKING);
            session.removeAttribute("bookingRequest");
            return ResponseEntity
                    .ok(new ApiResponseDTO<>(bookingID, "Xác nhận thanh toán thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO<>(null, e.getMessage()));
        }
    }

    @GetMapping("/check-refund/{bookingID}")
    public ResponseEntity<?> checkRefundForBooking(@PathVariable Long bookingID) {
        Booking booking = bookingService.getBookingByID(bookingID);

        Cancelability result = bookingService.checkCancelability(bookingID);

        switch (result) {
            case CANCELLED:
                return ResponseEntity.ok(new ApiResponseDTO<>(null, "Đơn đặt phòng đã bị hủy trước đó."));
            case COMPLETED:
                return ResponseEntity.ok(new ApiResponseDTO<>(null, "Đơn đặt phòng đã hoàn tất và không thể hủy."));
            case CHECKIN_TIME_PASSED:
                return ResponseEntity.ok(new ApiResponseDTO<>(null, "Đã quá thời gian nhận phòng, không thể hủy đơn."));
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

    @PostMapping("/cancel")
    public String postCancelBooking(@RequestParam("bookingID") long bookingID) {
        if (bookingService.checkCancelability(bookingID) == Cancelability.ALLOWED) {
            this.bookingService.cancelBooking(bookingID);
        }
        return "redirect:/admin/booking/" + bookingID;
    }

    @GetMapping("/schedule")
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

    @GetMapping("/can-add-service-or-extend/{bookingID}")
    @ResponseBody
    public ResponseEntity<Boolean> canAddServiceOrExtend(@PathVariable Long bookingID) {
        boolean result = bookingService.canBookServiceOrBookExtension(bookingID);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/booking-extension/create/{id}")
    public String getCreateBookingExtensionPage(Model model, @PathVariable Long id) {
        Booking booking = bookingService.getBookingByID(id);
        model.addAttribute("booking", booking);
        return "admin/booking-extension/create";
    }

    @PostMapping("/booking-extension/create")
    public String postBookingExtension(@RequestParam("bookingID") Long bookingID,
            @RequestParam("newCheckoutTime") @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime newCheckout,
            Model model) {
        Booking booking = bookingService.getBookingByID(bookingID);

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
            return "admin/booking-extension/create";
        }

        Float minutesDelay = (float) ChronoUnit.MINUTES.between(currentCheckout, newCheckout);
        Float hoursDelay = (float) (Math.ceil(minutesDelay / 30.0) * 0.5);
        if (hoursDelay <= 0) {
            model.addAttribute("errorMessage",
                    "Thời gian gia hạn không hợp lệ. Vui lòng chọn thời gian checkout mới sau thời gian hiện tại.");
            model.addAttribute("booking", booking);
            return "admin/booking-extension/create";
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
        return "admin/booking-extension/confirm-extension";
    }

    @PostMapping("/booking-extension/confirm-payment")
    public ResponseEntity<ApiResponseDTO<Long>> confirmPaymentExtension(
            @RequestBody PaymentConfirmationRequestDTO request) {
        try {
            Long extensionID = request.getExtensionID();
            BookingExtension bExtension = bookingExtensionService.getBookingExtensionByID(extensionID);
            Long bookingID = bExtension.getBooking().getBookingID();
            paymentService.handleSavePaymentWithAdmin(bookingID, request.getPaymentMethod(),
                    PaymentPurpose.EXTENDED_HOURS);
            return ResponseEntity
                    .ok(new ApiResponseDTO<>(bookingID, "Xác nhận thanh toán thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO<>(null, e.getMessage()));
        }
    }

    @PostMapping("/booking-extension/cancel")
    public String postCancelBookingExtension(@RequestParam("id") Long id, Model model) {
        Booking booking = bookingService
                .getBookingByID(bookingExtensionService.getBookingExtensionByID(id).getBooking().getBookingID());
        bookingExtensionService.deleteByExtensionID(id);
        return "redirect:/booking/booking-extension/create/" + booking.getBookingID();
    }

}
