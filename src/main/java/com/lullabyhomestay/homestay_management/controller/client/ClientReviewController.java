package com.lullabyhomestay.homestay_management.controller.client;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.Review;
import com.lullabyhomestay.homestay_management.domain.dto.CustomerDTO;
import com.lullabyhomestay.homestay_management.service.BookingService;
import com.lullabyhomestay.homestay_management.service.ReviewService;
import com.lullabyhomestay.homestay_management.service.UploadService;
import com.lullabyhomestay.homestay_management.service.UserService;
import com.lullabyhomestay.homestay_management.utils.AuthUtils;
import com.lullabyhomestay.homestay_management.utils.BookingUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@PreAuthorize("hasRole('CUSTOMER')")
public class ClientReviewController {
    private final ReviewService reviewService;
    private final BookingService bookingService;
    private final UploadService uploadService;
    private final ModelMapper mapper;
    private final UserService userService;

    @PostMapping("/review/create")
    public String postCreateReview(Model model,
            @ModelAttribute("newReview") @Valid Review review,
            BindingResult result,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(userService, mapper);
        if (result.hasErrors()) {
            Booking booking = bookingService.getBookingById(review.getBooking().getBookingId());
            BookingUtils.validateBooking(booking, customerDTO);
            BookingUtils.mapAndSetCustomerToBooking(booking, customerDTO, mapper);

            model.addAttribute("booking", booking);
            return "client/booking/detail-booking-history";
        }
        Booking booking = bookingService.getBookingById(review.getBooking().getBookingId());

        if (booking != null && booking.getStatus().toString().equals("COMPLETED")
                && booking.getCustomer().getCustomerId().equals(customerDTO.getCustomerId())) {
            if (!file.isEmpty()) {
                String img = this.uploadService.handleSaveUploadFile(file, "review");
                review.setImage(img);
            }
            reviewService.handleSaveReview(review);
        }
        return "redirect:/booking/booking-history/" + review.getBooking().getBookingId();
    }

    @PostMapping("/review/update")
    public String postUpdateReview(
            @ModelAttribute("editReview") @Valid Review review,
            BindingResult result,
            @RequestParam(value = "fileImg") MultipartFile file,
            Model model) {
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(userService, mapper);
        Long bookingId = review.getBooking().getBookingId();
        Long reviewId = review.getReviewId();

        if (result.hasErrors()) {
            Booking booking = bookingService.getBookingById(review.getBooking().getBookingId());
            BookingUtils.validateBooking(booking, customerDTO);
            BookingUtils.mapAndSetCustomerToBooking(booking, customerDTO, mapper);

            model.addAttribute("booking", booking);
            return "client/booking/detail-booking-history";
        }

        Booking booking = bookingService.getBookingById(bookingId);
        Review existingReview = reviewService.getReviewById(reviewId);

        if (booking != null && existingReview != null && booking.getStatus().toString().equals("COMPLETED")
                && booking.getCustomer().getCustomerId().equals(customerDTO.getCustomerId())) {
            existingReview.setRating(review.getRating());
            existingReview.setComment(review.getComment());

            if (!file.isEmpty()) {
                String img = this.uploadService.handleSaveUploadFile(file, "review");
                existingReview.setImage(img);
            }
            reviewService.handleSaveReview(existingReview);
        }
        return "redirect:/booking/booking-history/" + bookingId;
    }

    @PostMapping("/review/delete")
    public String deleteReview(@RequestParam("reviewId") Long reviewId) {
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(userService, mapper);
        Review review = reviewService.getReviewById(reviewId);
        BookingUtils.validateBooking(review.getBooking(), customerDTO);

        reviewService.deleteByReviewId(reviewId);
        return "redirect:/booking/booking-history/" + review.getBooking().getBookingId();
    }
}
