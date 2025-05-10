package com.lullabyhomestay.homestay_management.controller.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.Customer;
import com.lullabyhomestay.homestay_management.domain.dto.statistics.RevenueBreakdownDTO;
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
        Double totalRevenue = statisticsService.getTotalRevenue(startDate, endDate);
        Long countBookings = statisticsService.countBookingsByDateRange(startDate, endDate);
        RevenueBreakdownDTO breakdown = statisticsService.getRevenueBreakdown(startDate, endDate);
        Long countCustomers = statisticsService.countCustomersByDateRange(startDate, endDate);
        Long countReviews = statisticsService.countReviewsByDateRange(startDate, endDate);

        List<Object[]> topServices = statisticsService.getTop5MostUsedServices(startDate, endDate);
        List<Customer> topCustomers = statisticsService.getTop5CustomersByRewardPoints();
        List<Object[]> topRooms = statisticsService.getTop5MostBookedRooms(startDate, endDate);

        model.addAttribute("topServices", topServices);
        model.addAttribute("topCustomers", topCustomers);
        model.addAttribute("topRooms", topRooms);

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("countCustomers", countCustomers);
        model.addAttribute("countReviews", countReviews);
        model.addAttribute("roomRevenue", breakdown.getRoomRevenue());
        model.addAttribute("serviceRevenue", breakdown.getServiceRevenue());
        model.addAttribute("extensionRevenue", breakdown.getExtensionRevenue());
        model.addAttribute("countBookings", countBookings);
        model.addAttribute("timeRange", timeRange);
        return "admin/dashboard/show";
    }
}
