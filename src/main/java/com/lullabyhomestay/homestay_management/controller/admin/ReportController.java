//TODO: tách ra createria
package com.lullabyhomestay.homestay_management.controller.admin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.dto.statistics.ReportResultDTO;
import com.lullabyhomestay.homestay_management.domain.dto.statistics.RevenueStatisticsCriteriaDTO;
import com.lullabyhomestay.homestay_management.service.BranchService;
import com.lullabyhomestay.homestay_management.service.PaymentService;
import com.lullabyhomestay.homestay_management.service.StatisticsService;
import com.lullabyhomestay.homestay_management.utils.PaymentPurpose;
import com.lullabyhomestay.homestay_management.utils.ReportType;
import com.lullabyhomestay.homestay_management.utils.SplitTimeRangeConverter;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class ReportController {
    private final BranchService branchService;
    private final StatisticsService statisticsService;

    @GetMapping("/admin/report")
    public String showReport(@ModelAttribute RevenueStatisticsCriteriaDTO criteria, Model model) {
        if (criteria.getTimeRange() == null || criteria.getTimeRange().isEmpty()) {
            LocalDate now = LocalDate.now();
            criteria.setTimeRange(now.withDayOfMonth(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                    " - " + now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        ReportResultDTO reportResult = statisticsService.generateRevenueReport(criteria);

        model.addAttribute("branches", branchService.getAllBranches());
        model.addAttribute("paymentPurposes", PaymentPurpose.values());
        model.addAttribute("reportTypes", ReportType.values());
        model.addAttribute("reportData", reportResult);
        model.addAttribute("criteria", criteria);

        return "admin/report/show";
    }
}