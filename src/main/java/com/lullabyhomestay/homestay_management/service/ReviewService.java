package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.Review;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.BookingRepository;
import com.lullabyhomestay.homestay_management.repository.ReviewRepository;
import com.lullabyhomestay.homestay_management.utils.Constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;

    public Review handleSaveReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review getReviewByID(Long reviewID) {
        Optional<Review> reviewOpt = reviewRepository.findByReviewID(reviewID);
        if (!reviewOpt.isPresent()) {
            throw new NotFoundException("Đánh giá");
        }
        return reviewOpt.get();
    }

    public List<Review> getReviewsByRoomID(Long roomID) {
        return reviewRepository.findByRoomID(roomID);
    }

    @Transactional
    public void deleteByReviewID(Long reviewID) {
        Review review = getReviewByID(reviewID);
        Booking booking = review.getBooking();
        if (booking != null) {
            booking.setReview(null);
            bookingRepository.save(booking);
        }
        reviewRepository.deleteByReviewID(reviewID);
    }

    public List<Review> getAllFiveStarReviews() {
        return reviewRepository.findTop10ByRatingOrderByCreatedAtDesc(5);
    }

    public Page<Review> searchReviews(Long branchID, String sortOrder, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE,
                "asc".equals(sortOrder) ? Sort.by("rating").ascending()
                        : "desc".equals(sortOrder) ? Sort.by("rating").descending() : Sort.unsorted());
        if (branchID == null)
            return reviewRepository
                    .findAll(pageable);
        return (branchID == null)
                ? reviewRepository.findAll(pageable)
                : reviewRepository.findByBranchID(branchID, pageable);
    }
}
