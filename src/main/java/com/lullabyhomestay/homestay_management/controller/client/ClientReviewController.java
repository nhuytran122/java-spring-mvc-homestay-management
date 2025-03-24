package com.lullabyhomestay.homestay_management.controller.client;

import org.modelmapper.ModelMapper;
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
import com.lullabyhomestay.homestay_management.service.CustomerService;
import com.lullabyhomestay.homestay_management.service.ReviewService;
import com.lullabyhomestay.homestay_management.service.UploadService;
import com.lullabyhomestay.homestay_management.utils.AuthUtils;
import com.lullabyhomestay.homestay_management.utils.BookingUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class ClientReviewController {
    private final ReviewService reviewService;
    private final BookingService bookingService;
    private final CustomerService customerService;
    private final UploadService uploadService;
    private final ModelMapper mapper;

    @PostMapping("/review/create")
    public String postCreateReview(Model model,
            @ModelAttribute("newReview") @Valid Review review,
            BindingResult result,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {
        if (result.hasErrors()) {
            Booking booking = bookingService.getBookingByID(review.getBooking().getBookingID());
            CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(customerService);
            BookingUtils.validateBooking(booking, customerDTO);
            BookingUtils.mapAndSetCustomerToBooking(booking, customerDTO, mapper);

            model.addAttribute("booking", booking);
            model.addAttribute("numberOfHours", booking.getNumberOfHours());
            return "client/booking/detail-booking-history";
        }
        Booking booking = bookingService.getBookingByID(review.getBooking().getBookingID());
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(customerService);

        if (booking != null && booking.getStatus().toString().equals("COMPLETED")
                && booking.getCustomer().getCustomerID().equals(customerDTO.getCustomerID())) {
            if (!file.isEmpty()) {
                String img = this.uploadService.handleSaveUploadFile(file, "review");
                review.setImage(img);
            }
            reviewService.handleSaveReview(review);
        }
        return "redirect:/booking/booking-history/" + review.getBooking().getBookingID();
    }

    @PostMapping("/review/update")
    public String postUpdateReview(
            @ModelAttribute("editReview") @Valid Review review,
            BindingResult result,
            @RequestParam(value = "fileImg") MultipartFile file,
            Model model) {
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(customerService);
        Long bookingID = review.getBooking().getBookingID();
        Long reviewID = review.getReviewID();

        if (result.hasErrors()) {
            Booking booking = bookingService.getBookingByID(review.getBooking().getBookingID());
            BookingUtils.validateBooking(booking, customerDTO);
            BookingUtils.mapAndSetCustomerToBooking(booking, customerDTO, mapper);

            model.addAttribute("booking", booking);
            model.addAttribute("numberOfHours", booking.getNumberOfHours());
            return "client/booking/detail-booking-history";
        }

        Booking booking = bookingService.getBookingByID(bookingID);
        Review existingReview = reviewService.getReviewByID(reviewID);

        if (booking != null && existingReview != null && booking.getStatus().toString().equals("COMPLETED")
                && booking.getCustomer().getCustomerID().equals(customerDTO.getCustomerID())) {
            existingReview.setRating(review.getRating());
            existingReview.setComment(review.getComment());

            if (!file.isEmpty()) {
                String img = this.uploadService.handleSaveUploadFile(file, "review");
                existingReview.setImage(img);
            }
            reviewService.handleSaveReview(existingReview);
        }
        return "redirect:/booking/booking-history/" + bookingID;
    }

    @PostMapping("/review/delete")
    public String deleteReview(@RequestParam("reviewID") Long reviewID) {
        CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(customerService);
        Review review = reviewService.getReviewByID(reviewID);
        BookingUtils.validateBooking(review.getBooking(), customerDTO);

        reviewService.deleteByReviewID(reviewID);
        return "redirect:/booking/booking-history/" + review.getBooking().getBookingID();
    }
}
