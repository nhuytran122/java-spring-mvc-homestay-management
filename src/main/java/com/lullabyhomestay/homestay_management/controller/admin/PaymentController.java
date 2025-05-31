package com.lullabyhomestay.homestay_management.controller.admin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.Payment;
import com.lullabyhomestay.homestay_management.domain.dto.SearchPaymentCriteriaDTO;
import com.lullabyhomestay.homestay_management.service.PaymentService;
import com.lullabyhomestay.homestay_management.utils.PaymentPurpose;
import com.lullabyhomestay.homestay_management.utils.PaymentStatus;
import com.lullabyhomestay.homestay_management.utils.PaymentType;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
@RequestMapping("/admin/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("")
    public String getBookingPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @ModelAttribute SearchPaymentCriteriaDTO criteria) {
        int validPage = Math.max(1, page);

        if (criteria.getTimeRange() == null || criteria.getTimeRange().isEmpty()) {
            LocalDateTime startDefault = LocalDateTime.now().minusDays(7);
            LocalDateTime endDefault = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            criteria.setTimeRange(startDefault.format(formatter) + " - " + endDefault.format(formatter));
        }
        if (criteria.getFromTime().isAfter(criteria.getToTime())) {
            model.addAttribute("errorMessage", "Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc!");
            return prepareModelWithoutSearch(model, criteria, validPage);
        }

        Page<Payment> payments = paymentService.searchPayments(criteria, validPage);
        List<Payment> listPayments = payments.getContent();
        model.addAttribute("totalPages", payments.getTotalPages());
        model.addAttribute("listPayments", listPayments);
        return prepareModelWithoutSearch(model, criteria, validPage);
    }

    private String prepareModelWithoutSearch(Model model, SearchPaymentCriteriaDTO criteria, int validPage) {
        model.addAttribute("criteria", criteria);
        model.addAttribute("extraParams", criteria.convertToExtraParams());
        model.addAttribute("currentPage", validPage);
        model.addAttribute("paymentStatuses", PaymentStatus.values());
        model.addAttribute("paymentTypes", PaymentType.values());
        return "admin/payment/show";
    }

    @GetMapping("/{id}")
    public String getDetailPaymentPage(Model model, @PathVariable long id) {
        Payment payment = paymentService.getPaymentById(id);
        model.addAttribute("payment", payment);
        return "admin/payment/detail";
    }

    @PostMapping("/handle")
    public String handlePayment(
            @RequestParam("bookingId") Long bookingId,
            @RequestParam("purpose") PaymentPurpose purpose,
            @RequestParam("paymentType") PaymentType paymentType) {
        paymentService.handleSavePaymentWithAdmin(bookingId, paymentType, purpose);
        return "redirect:/admin/booking/" + bookingId;
    }

}
