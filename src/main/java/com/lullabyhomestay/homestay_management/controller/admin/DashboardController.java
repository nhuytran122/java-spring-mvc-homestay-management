package com.lullabyhomestay.homestay_management.controller.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.dto.statistics.DashboardStatisticsDTO;
import com.lullabyhomestay.homestay_management.service.StatisticsService;
import com.lullabyhomestay.homestay_management.utils.SplitTimeRangeConverter;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class DashboardController {

    private final StatisticsService statisticsService;

    @GetMapping("/admin/dashboard")
    public String getDashboardPage(
            @RequestParam(value = "timeRange", required = false) String timeRange,
            Model model) {
        LocalDateTime startDate;
        LocalDateTime endDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (timeRange == null || timeRange.isEmpty()) {
            startDate = LocalDate.now().withDayOfMonth(1).atStartOfDay();
            endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59);
            timeRange = startDate.format(formatter) + " - " + endDate.format(formatter);
        } else {
            try {
                startDate = SplitTimeRangeConverter.parseStartDate(timeRange);
                endDate = SplitTimeRangeConverter.parseEndDate(timeRange);
            } catch (Exception e) {
                startDate = LocalDate.now().withDayOfMonth(1).atStartOfDay();
                endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59);
                timeRange = startDate.format(formatter) + " - " + endDate.format(formatter);
            }
        }
        DashboardStatisticsDTO stats = statisticsService.getDashboardStatistics(startDate, endDate);
        model.addAttribute("stats", stats);
        model.addAttribute("timeRange", timeRange);
        return "admin/dashboard/show";
    }
}
