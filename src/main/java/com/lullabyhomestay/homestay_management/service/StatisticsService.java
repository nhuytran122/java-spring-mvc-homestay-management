package com.lullabyhomestay.homestay_management.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;

import com.lullabyhomestay.homestay_management.domain.Customer;
import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.domain.Service;
import com.lullabyhomestay.homestay_management.domain.dto.statistics.RevenueBreakdownDTO;
import com.lullabyhomestay.homestay_management.repository.BookingRepository;
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
        return bookingRepository.countBookingsByDateRange(startDate, endDate);
    }

    public Long countCustomersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        Long countCustomers = customerRepository.countNewCustomers(startDate, endDate);
        return countCustomers;
    }

    public Long countReviewsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        Long countReviews = reviewRepository.countReviews(startDate, endDate);
        return countReviews;
    }

    public List<Object[]> getTop5MostBookedRooms(LocalDateTime startDate, LocalDateTime endDate) {
        return roomRepository.findTop5RoomsWithCount(startDate, endDate);
    }

    public List<Object[]> getTop5MostUsedServices(LocalDateTime startDate, LocalDateTime endDate) {
        return serviceRepository.findTopServicesWithCount(startDate, endDate);
    }

    public List<Customer> getTop5CustomersByRewardPoints() {
        return customerRepository.findTop5CustomersByRewardPoints();
    }

}
