package com.lullabyhomestay.homestay_management.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.lullabyhomestay.homestay_management.domain.Customer;
import com.lullabyhomestay.homestay_management.domain.dto.statistics.DashboardStatisticsDTO;
import com.lullabyhomestay.homestay_management.domain.dto.statistics.ReportResultDTO;
import com.lullabyhomestay.homestay_management.domain.dto.statistics.RevenueBreakdownDTO;
import com.lullabyhomestay.homestay_management.domain.dto.statistics.RevenueStatisticsCriteriaDTO;
import com.lullabyhomestay.homestay_management.repository.BookingRepository;
import com.lullabyhomestay.homestay_management.repository.BookingServiceRepository;
import com.lullabyhomestay.homestay_management.repository.CustomerRepository;
import com.lullabyhomestay.homestay_management.repository.PaymentDetailRepository;
import com.lullabyhomestay.homestay_management.repository.PaymentRepository;
import com.lullabyhomestay.homestay_management.repository.ReviewRepository;
import com.lullabyhomestay.homestay_management.repository.RoomRepository;
import com.lullabyhomestay.homestay_management.repository.ServiceRepository;
import com.lullabyhomestay.homestay_management.utils.PaymentPurpose;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@org.springframework.stereotype.Service
public class StatisticsService {
    private final PaymentRepository paymentRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final ReviewRepository reviewRepository;
    private final RoomRepository roomRepository;
    private final ServiceRepository serviceRepository;
    private final BookingServiceRepository bookingServiceRepository;

    public Double getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        Double totalPayment = paymentRepository.getTotalPaymentAmount(startDate, endDate);
        return totalPayment != null ? totalPayment : 0.0;
    }

    public RevenueBreakdownDTO getRevenueBreakdown(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> paymentResults = paymentDetailRepository.getPaymentAmountByPurpose(startDate, endDate);
        Map<PaymentPurpose, Double> paymentMap = new HashMap<>();
        for (Object[] row : paymentResults) {
            PaymentPurpose purpose = (PaymentPurpose) row[0];
            Double amount = (Double) row[1];
            paymentMap.put(purpose, amount != null ? amount : 0.0);
        }
        Double postPaidServiceRevenue = paymentMap.getOrDefault(PaymentPurpose.ADDITIONAL_SERVICE, 0.0);
        Double prepaidServiceRevenue = paymentMap.getOrDefault(PaymentPurpose.PREPAID_SERVICE, 0.0);
        Double serviceRevenue = prepaidServiceRevenue + postPaidServiceRevenue;
        Double roomRevenue = paymentMap.getOrDefault(PaymentPurpose.ROOM_BOOKING, 0.0);

        Double extensionRevenue = paymentMap.getOrDefault(PaymentPurpose.EXTENDED_HOURS, 0.0);

        return new RevenueBreakdownDTO(roomRevenue, serviceRevenue, extensionRevenue);
    }

    public Long countBookingsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        Long countBookings = bookingRepository.countBookingsByDateRange(startDate, endDate);
        return countBookings;
    }

    // public Long countCustomersByDateRange(LocalDateTime startDate, LocalDateTime
    // endDate) {
    // Long countCustomers = customerRepository.countNewCustomers(startDate,
    // endDate);
    // return countCustomers;
    // }

    public Long countReviewsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        Long countReviews = reviewRepository.countReviews(startDate, endDate);
        return countReviews;
    }

    public Long countPendingBookingServices(LocalDateTime startDate, LocalDateTime endDate) {
        Long countPending = bookingServiceRepository.countPendingServicesByDateRange(startDate, endDate);
        return countPending;
    }

    // public List<Object[]> getTop5MostBookedRooms(LocalDateTime startDate,
    // LocalDateTime endDate) {
    // return roomRepository.findTop5RoomsWithCount(startDate, endDate);
    // }

    public List<Object[]> getTop5MostBookedRooms(LocalDateTime startDate, LocalDateTime endDate) {
        Pageable pageable = PageRequest.of(0, 5);
        return roomRepository.findTopRoomsWithCount(startDate, endDate, pageable);
    }

    // public List<Object[]> getTop5MostUsedServices(LocalDateTime startDate,
    // LocalDateTime endDate) {
    // return serviceRepository.findTopServicesWithCount(startDate, endDate);
    // }

    public List<Object[]> getTop5MostUsedServices(LocalDateTime startDate, LocalDateTime endDate) {
        Pageable pageable = PageRequest.of(0, 5);
        return serviceRepository.findTopServicesWithCount(startDate, endDate, pageable);
    }

    public List<Customer> getTop5CustomersByRewardPoints() {
        Pageable pageable = PageRequest.of(0, 5);
        return customerRepository.findTopCustomersByRewardPoints(pageable);
    }

    public BigDecimal getSumRevenue(Long branchID, PaymentPurpose purpose,
            LocalDateTime start,
            LocalDateTime end) {
        return paymentDetailRepository.sumRevenue(branchID, purpose, start, end);
    }

    public DashboardStatisticsDTO getDashboardStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        Double totalRevenue = getTotalRevenue(startDate, endDate);
        Long countBookings = countBookingsByDateRange(startDate, endDate);
        RevenueBreakdownDTO breakdown = getRevenueBreakdown(startDate, endDate);
        // Long countCustomers = countCustomersByDateRange(startDate, endDate);
        Long countReviews = countReviewsByDateRange(startDate, endDate);
        Long countPendingBookingServices = countPendingBookingServices(startDate, endDate);
        List<Object[]> topServices = getTop5MostUsedServices(startDate, endDate);
        List<Customer> topCustomers = getTop5CustomersByRewardPoints();
        List<Object[]> topRooms = getTop5MostBookedRooms(startDate, endDate);

        return new DashboardStatisticsDTO(
                totalRevenue,
                countBookings,
                breakdown,
                countReviews,
                countPendingBookingServices,
                topServices,
                topCustomers,
                topRooms);
    }

    public ReportResultDTO generateRevenueReport(RevenueStatisticsCriteriaDTO criteria) {
        validateDateRange(criteria);

        List<String> labels = new ArrayList<>();
        List<BigDecimal> revenues = new ArrayList<>();

        switch (criteria.getType()) {
            case DAILY:
                generateDailyReport(criteria, labels, revenues);
                break;
            case MONTHLY:
                generateMonthlyReport(criteria, labels, revenues);
                break;
            case YEARLY:
                generateYearlyReport(criteria, labels, revenues);
                break;
        }

        BigDecimal total = calculateTotalRevenue(revenues);

        return new ReportResultDTO(
                labels,
                revenues,
                total);
    }

    private void generateDailyReport(RevenueStatisticsCriteriaDTO criteria,
            List<String> labels,
            List<BigDecimal> revenues) {
        for (LocalDate date = criteria.getStartDate(); !date.isAfter(criteria.getEndDate()); date = date.plusDays(1)) {

            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay();

            BigDecimal revenue = fetchRevenue(criteria, start, end);
            labels.add(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
            revenues.add(revenue);
        }
    }

    private void generateMonthlyReport(RevenueStatisticsCriteriaDTO criteria,
            List<String> labels,
            List<BigDecimal> revenues) {
        YearMonth startMonth = YearMonth.from(criteria.getStartDate());
        YearMonth endMonth = YearMonth.from(criteria.getEndDate());

        for (YearMonth month = startMonth; !month.isAfter(endMonth); month = month.plusMonths(1)) {
            LocalDateTime start = month.atDay(1).atStartOfDay();
            LocalDateTime end = month.plusMonths(1).atDay(1).atStartOfDay();

            BigDecimal revenue = fetchRevenue(criteria, start, end);
            labels.add(month.toString());
            revenues.add(revenue);
        }
    }

    private void generateYearlyReport(RevenueStatisticsCriteriaDTO criteria,
            List<String> labels,
            List<BigDecimal> revenues) {
        int startYear = criteria.getStartDate().getYear();
        int endYear = criteria.getEndDate().getYear();

        for (int year = startYear; year <= endYear; year++) {
            LocalDateTime start = LocalDate.of(year, 1, 1).atStartOfDay();
            LocalDateTime end = LocalDate.of(year + 1, 1, 1).atStartOfDay();

            BigDecimal revenue = fetchRevenue(criteria, start, end);
            labels.add(String.valueOf(year));
            revenues.add(revenue);
        }
    }

    private BigDecimal fetchRevenue(RevenueStatisticsCriteriaDTO criteria,
            LocalDateTime start,
            LocalDateTime end) {
        BigDecimal result = getSumRevenue(
                criteria.getBranchId(),
                criteria.getPurpose(),
                start,
                end);
        result = result != null ? result : BigDecimal.ZERO;
        return result;
    }

    private void validateDateRange(RevenueStatisticsCriteriaDTO criteria) {
        if (criteria.getEndDate().isBefore(criteria.getStartDate())) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
        }
    }

    private BigDecimal calculateTotalRevenue(List<BigDecimal> revenues) {
        return revenues.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
