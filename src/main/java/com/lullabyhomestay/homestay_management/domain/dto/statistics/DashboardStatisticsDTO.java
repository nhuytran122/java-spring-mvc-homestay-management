package com.lullabyhomestay.homestay_management.domain.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import com.lullabyhomestay.homestay_management.domain.Customer;

@Data
@AllArgsConstructor
public class DashboardStatisticsDTO {
    private Double totalRevenue;
    private Long countBookings;
    private RevenueBreakdownDTO revenueBreakdown;
    private Long countCustomers;
    private Long countReviews;
    private List<Object[]> topServices;
    private List<Customer> topCustomers;
    private List<Object[]> topRooms;
}