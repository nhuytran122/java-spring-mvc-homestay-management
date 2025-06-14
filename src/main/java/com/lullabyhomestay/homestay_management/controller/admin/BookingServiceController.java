package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lullabyhomestay.homestay_management.domain.BookingServices;
import com.lullabyhomestay.homestay_management.domain.dto.SearchBookingServiceCriteriaDTO;
import com.lullabyhomestay.homestay_management.service.BookingExtraService;
import com.lullabyhomestay.homestay_management.service.BookingService;
import com.lullabyhomestay.homestay_management.service.HomestayServiceService;
import com.lullabyhomestay.homestay_management.service.validator.AdminValidation;
import com.lullabyhomestay.homestay_management.utils.BookingServiceStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/admin/booking-service")
public class BookingServiceController {
    private final BookingExtraService bookingExtraService;
    private final HomestayServiceService homestayServiceService;
    private final BookingService bookingService;

    @GetMapping("")
    public String getBookingServicesPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @ModelAttribute SearchBookingServiceCriteriaDTO criteria) {
        int validPage = Math.max(1, page);
        String sort = (criteria.getSort() != null && !criteria.getSort().isEmpty()) ? criteria.getSort() : "desc";
        criteria.setSort(sort);

        Page<BookingServices> bookings = bookingExtraService.searchBookingServices(criteria, validPage);
        List<BookingServices> listBookingServices = bookings.getContent();
        model.addAttribute("totalPages", bookings.getTotalPages());
        model.addAttribute("listBookingServices", listBookingServices);
        return prepareModelWithoutSearch(model, criteria, validPage);
    }

    private String prepareModelWithoutSearch(Model model, SearchBookingServiceCriteriaDTO criteria, int validPage) {
        model.addAttribute("criteria", criteria);
        model.addAttribute("extraParams", criteria.convertToExtraParams());
        model.addAttribute("currentPage", validPage);
        model.addAttribute("statuses", BookingServiceStatus.values());
        return "admin/booking-service/show";
    }

    @GetMapping("/create/{id}")
    public String getCreateBookingServicePage(Model model, @PathVariable long id) {
        BookingServices newBookingServices = new BookingServices();
        boolean canBook = bookingService.canBookServiceOrBookExtension(id);
        newBookingServices.setBooking(bookingService.getBookingById(id));
        model.addAttribute("newBookingService", newBookingServices);
        if (!canBook) {
            model.addAttribute("canBook", canBook);
        } else {
            model.addAttribute("listServices", homestayServiceService.getAllServices());
        }
        return "admin/booking-service/create";
    }

    @PostMapping("/create")
    public String postCreateBookingService(Model model,
            @ModelAttribute("newBookingService") @Validated(AdminValidation.class) BookingServices bookingService,
            BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("listServices", homestayServiceService.getAllServices());
            return "admin/booking-service/create";
        }
        bookingExtraService.handleSaveBookingServiceExtra(bookingService);
        return "redirect:/admin/booking/" + bookingService.getBooking().getBookingId();
    }

    @GetMapping("/update/{id}")
    public String getUpdateBookingServicePage(Model model, @PathVariable long id) {
        BookingServices bookingService = bookingExtraService.getBookingServiceById(id);
        model.addAttribute("bookingService", bookingService);
        boolean canUpdate = bookingExtraService.canUpdateAndDeleteBookingService(id);
        model.addAttribute("canUpdate", canUpdate);
        return "admin/booking-service/update";
    }

    @PostMapping("/update")
    public String postUpdateBookingService(Model model,
            @ModelAttribute("bookingService") @Validated(AdminValidation.class) @Valid BookingServices bookingService,
            BindingResult result,
            HttpServletRequest request) {

        Long bookingServiceId = bookingService.getBookingServiceId();
        BookingServices currentBookingService = this.bookingExtraService.getBookingServiceById(bookingServiceId);
        if (result.hasErrors()) {
            return "admin/booking-service/update";
        }
        currentBookingService.setDescription(bookingService.getDescription());
        this.bookingExtraService.updateQuantityBookingServices(currentBookingService, bookingService.getQuantity());
        return "redirect:/admin/booking/" + currentBookingService.getBooking().getBookingId();
    }

    @GetMapping("/can-handle/{id}")
    public ResponseEntity<Boolean> canHandleBookingService(@PathVariable long id) {
        boolean canHandle = bookingExtraService.canUpdateAndDeleteBookingService(id);
        return ResponseEntity.ok(canHandle);
    }

    @PostMapping("/delete")
    public String postDeleteBranch(@RequestParam("bookingServiceId") Long bookingServiceId,
            HttpServletRequest request) {
        this.bookingExtraService.deleteBookingServiceById(bookingServiceId);
        return redirectToReferer(request, "admin/booking-service");
    }

    private String redirectToReferer(HttpServletRequest request, String fallbackUrl) {
        String referer = request.getHeader("Referer");
        if (referer == null || referer.isEmpty()) {
            return "redirect:" + fallbackUrl;
        }
        return "redirect:" + referer;
    }

    @PostMapping("/update-status")
    @ResponseBody
    public ResponseEntity<?> updateBookingServiceStatus(
            @RequestParam("bookingServiceId") Long bookingServiceId,
            @RequestParam("status") String status) {
        BookingServices bService = bookingExtraService.getBookingServiceById(bookingServiceId);
        BookingServiceStatus currentStatus = bService.getStatus();
        BookingServiceStatus newStatus = BookingServiceStatus.valueOf(status.toUpperCase());

        if (currentStatus == BookingServiceStatus.COMPLETED) {
            return ResponseEntity.badRequest().body("Đơn đặt dịch vụ này đã hoàn tất, không thể cập nhật!");
        }
        if (currentStatus == BookingServiceStatus.CANCELLED) {
            return ResponseEntity.badRequest().body("Đơn đặt dịch vụ này đã bị hủy, không thể cập nhật!");
        }
        if (currentStatus == BookingServiceStatus.PENDING && newStatus == BookingServiceStatus.PENDING) {
            return ResponseEntity.badRequest().body("Đơn đặt dịch vụ này đã đang chờ xử lý, không cần cập nhật lại!");
        }
        if (currentStatus == BookingServiceStatus.IN_PROGRESS &&
                (newStatus == BookingServiceStatus.PENDING || newStatus == BookingServiceStatus.IN_PROGRESS)) {
            return ResponseEntity.badRequest().body("Đang xử lý, không thể quay lại chờ hoặc giữ nguyên!");
        }

        if (newStatus == BookingServiceStatus.COMPLETED && bService.getQuantity() == null) {
            return ResponseEntity.badRequest().body("Không thể cập nhật sang hoàn thành vì chưa có số lượng yêu cầu!");
        }
        bService.setStatus(newStatus);
        bookingExtraService.handleUpdateStatusBookingService(bService);
        return ResponseEntity.ok().build();
    }
}
