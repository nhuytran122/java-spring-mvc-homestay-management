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
public class BookingServiceController {
    private final BookingExtraService bookingExtraService;
    private final HomestayServiceService homestayServiceService;
    private final BookingService bookingService;

    @GetMapping("/admin/booking-service")
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

    @GetMapping("/admin/booking-service/create/{id}")
    public String getCreateBookingServicePage(Model model, @PathVariable long id) {
        BookingServices newBookingServices = new BookingServices();
        boolean canBook = bookingService.canBookServiceOrBookExtension(id);
        newBookingServices.setBooking(bookingService.getBookingByID(id));
        model.addAttribute("newBookingService", newBookingServices);
        if (!canBook) {
            model.addAttribute("canBook", canBook);
        } else {
            model.addAttribute("listServices", homestayServiceService.getAllServices());
        }
        return "admin/booking-service/create";
    }

    @PostMapping("/admin/booking-service/create")
    public String postCreateBookingService(Model model,
            @ModelAttribute("newBookingService") @Validated(AdminValidation.class) BookingServices bookingService,
            BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("listServices", homestayServiceService.getAllServices());
            return "admin/booking-service/create";
        }
        bookingExtraService.handleSaveBookingServiceExtra(bookingService);
        return "redirect:/admin/booking/" + bookingService.getBooking().getBookingID();
    }

    @GetMapping("/admin/booking-service/update/{id}")
    public String getUpdateBookingServicePage(Model model, @PathVariable long id) {
        BookingServices bookingService = bookingExtraService.getBookingServiceByID(id);
        model.addAttribute("bookingService", bookingService);
        boolean canUpdate = bookingExtraService.canUpdateAndDeleteBookingService(id);
        model.addAttribute("canUpdate", canUpdate);
        return "admin/booking-service/update";
    }

    @PostMapping("/admin/booking-service/update")
    public String postUpdateBookingService(Model model,
            @ModelAttribute("bookingService") @Validated(AdminValidation.class) @Valid BookingServices bookingService,
            BindingResult result,
            HttpServletRequest request) {

        Long bookingServiceID = bookingService.getBookingServiceID();
        BookingServices currentBookingService = this.bookingExtraService.getBookingServiceByID(bookingServiceID);
        if (result.hasErrors()) {
            return "admin/booking-service/update";
        }
        currentBookingService.setDescription(bookingService.getDescription());
        this.bookingExtraService.updateQuantityBookingServices(currentBookingService, bookingService.getQuantity());
        return "redirect:/admin/booking/" + currentBookingService.getBooking().getBookingID();
    }

    @GetMapping("/admin/booking-service/can-handle/{id}")
    public ResponseEntity<Boolean> canHandleBookingService(@PathVariable long id) {
        boolean canHandle = bookingExtraService.canUpdateAndDeleteBookingService(id);
        return ResponseEntity.ok(canHandle);
    }

    @PostMapping("/admin/booking-service/delete")
    public String postDeleteBranch(@RequestParam("bookingServiceID") Long bookingServiceID,
            HttpServletRequest request) {
        this.bookingExtraService.deleteBookingServiceByID(bookingServiceID);
        return redirectToReferer(request, "admin/booking-service");
    }

    private String redirectToReferer(HttpServletRequest request, String fallbackUrl) {
        String referer = request.getHeader("Referer");
        if (referer == null || referer.isEmpty()) {
            return "redirect:" + fallbackUrl;
        }
        return "redirect:" + referer;
    }

    @PostMapping("/admin/booking-service/update-status")
    @ResponseBody
    public ResponseEntity<?> updateBookingServiceStatus(
            @RequestParam("bookingServiceID") Long bookingServiceID,
            @RequestParam("status") String status) {
        BookingServices bService = bookingExtraService.getBookingServiceByID(bookingServiceID);
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
        bService.setStatus(newStatus);
        bookingExtraService.handleUpdateStatusBookingService(bService);
        return ResponseEntity.ok().build();
    }
}
