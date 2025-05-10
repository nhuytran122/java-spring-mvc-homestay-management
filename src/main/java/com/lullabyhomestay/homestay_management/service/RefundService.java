package com.lullabyhomestay.homestay_management.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.Refund;
import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.domain.dto.SearchRefundCriteriaDTO;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.RefundRepository;
import com.lullabyhomestay.homestay_management.service.specifications.RefundSpecification;
import com.lullabyhomestay.homestay_management.utils.Constants;
import com.lullabyhomestay.homestay_management.utils.PaymentStatus;
import com.lullabyhomestay.homestay_management.utils.RefundType;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RefundService {
    private final RefundRepository refundRepository;

    public Page<Refund> searchRefunds(SearchRefundCriteriaDTO criteria, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE, Sort.by("CreatedAt").descending());

        boolean isAllCriteriaEmpty = (criteria.getKeyword() == null || criteria.getKeyword().isEmpty())
                && (criteria.getTimeRange() == null || criteria.getTimeRange().isEmpty())
                && (criteria.getStatus() == null || criteria.getStatus().isEmpty())
                && (criteria.getType() == null || criteria.getType().isEmpty());

        if (isAllCriteriaEmpty) {
            return refundRepository.findAll(pageable);
        }

        PaymentStatus status = null;
        if (criteria.getStatus() != null && !criteria.getStatus().isEmpty()) {
            try {
                status = PaymentStatus.valueOf(criteria.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                status = null;
            }
        }

        RefundType type = null;
        if (criteria.getType() != null && !criteria.getType().isEmpty()) {
            try {
                type = RefundType.valueOf(criteria.getType().toUpperCase());
            } catch (IllegalArgumentException e) {
                type = null;
            }
        }

        Specification<Refund> spec = Specification.where(RefundSpecification.statusEqual(status))
                .and(RefundSpecification.typeEqual(type))
                .and(RefundSpecification.refundDateBetween(criteria.getFromTime(), criteria.getToTime()));

        return refundRepository.findAll(spec, pageable);
    }

    public RefundType getRefundType(Booking booking) {
        LocalDateTime checkIn = booking.getCheckIn();
        LocalDateTime now = LocalDateTime.now();
        long daysDiff = ChronoUnit.DAYS.between(now.toLocalDate(), checkIn.toLocalDate());

        if (daysDiff > 7)
            return RefundType.FULL;
        else if (daysDiff > 3)
            return RefundType.PARTIAL_70;
        else
            return RefundType.PARTIAL_30;
    }

    public Double calculateRefundAmount(Booking booking) {
        RefundType type = getRefundType(booking);
        double paid = booking.getPaidAmount();

        switch (type) {
            case FULL:
                return paid;
            case PARTIAL_70:
                return paid * 0.7;
            case PARTIAL_30:
                return paid * 0.3;
            default:
                return 0.0;
        }
    }

    public Refund getRefundByID(Long id) {
        Optional<Refund> refundOpt = this.refundRepository.findById(id);
        if (!refundOpt.isPresent()) {
            throw new NotFoundException("Yêu cầu hoàn tiền");
        }
        return refundOpt.get();
    }

}
