package com.lullabyhomestay.homestay_management.controller.admin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.Refund;
import com.lullabyhomestay.homestay_management.domain.dto.SearchRefundCriteriaDTO;
import com.lullabyhomestay.homestay_management.service.RefundService;
import com.lullabyhomestay.homestay_management.utils.RefundType;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class RefundController {
    private final RefundService refundService;

    @GetMapping("/admin/refund")
    public String getBookingPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @ModelAttribute SearchRefundCriteriaDTO criteria) {
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

        Page<Refund> refunds = refundService.searchRefunds(criteria, validPage);
        List<Refund> listRefunds = refunds.getContent();
        model.addAttribute("totalPages", refunds.getTotalPages());
        model.addAttribute("listRefunds", listRefunds);
        return prepareModelWithoutSearch(model, criteria, validPage);
    }

    @GetMapping("/admin/refund/{id}")
    public String getDetailRefundPage(Model model, @PathVariable Long id) {
        Refund refund = refundService.getRefundByID(id);
        model.addAttribute("refund", refund);
        return "admin/refund/detail";
    }

    private String prepareModelWithoutSearch(Model model, SearchRefundCriteriaDTO criteria, int validPage) {
        model.addAttribute("criteria", criteria);
        model.addAttribute("extraParams", criteria.convertToExtraParams());
        model.addAttribute("currentPage", validPage);
        model.addAttribute("refundTypes", RefundType.values());
        return "admin/refund/show";
    }
}
