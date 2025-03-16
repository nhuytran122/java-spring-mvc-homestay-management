package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.BookingServices;
import com.lullabyhomestay.homestay_management.domain.dto.SearchBookingServiceCriteriaDTO;
import com.lullabyhomestay.homestay_management.service.BookingExtraService;
import com.lullabyhomestay.homestay_management.service.HomestayServiceService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class BookingServiceController {
    private final BookingExtraService bookingExtraService;
    private final HomestayServiceService homestayServiceService;

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
        return "admin/booking-service/show";
    }

    @GetMapping("/admin/booking-service/update/{id}")
    public String getUpdateBookingServicePage(Model model, @PathVariable long id) {
        BookingServices bookingService = bookingExtraService.getBookingServiceByID(id);
        model.addAttribute("bookingService", bookingService);
        model.addAttribute("listServices", this.homestayServiceService.getAllServices());
        return "admin/booking-service/update";
    }

    @PostMapping("/admin/booking-service/update")
    public String postUpdateBookingService(Model model,
            @ModelAttribute("bookingService") @Valid BookingServices bookingService,
            BindingResult result,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        Long bookingServiceID = bookingService.getBookingServiceID();
        BookingServices currentBookingService = this.bookingExtraService.getBookingServiceByID(bookingServiceID);
        if (result.hasErrors()) {
            model.addAttribute("listServices", this.homestayServiceService.getAllServices());
            return "admin/booking-service/update";
        }
        currentBookingService.setQuantity(bookingService.getQuantity());
        currentBookingService.setDescription(bookingService.getDescription());
        currentBookingService.setService(bookingService.getService());
        this.bookingExtraService.handleSaveBookingServiceExtra(currentBookingService);
        return "redirect:/admin/booking-service";
    }
}
