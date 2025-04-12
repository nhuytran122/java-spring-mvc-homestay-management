package com.lullabyhomestay.homestay_management.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.lullabyhomestay.homestay_management.domain.Review;
import com.lullabyhomestay.homestay_management.service.BranchService;
import com.lullabyhomestay.homestay_management.service.ReviewService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final BranchService branchService;

    @GetMapping("/admin/review")
    public String getReviewsPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String sort,
            @RequestParam(required = false) Long branchID) {
        int validPage = Math.max(1, page);

        Page<Review> reviews = reviewService.searchReviews(branchID, sort, validPage);

        StringBuilder extraParams = new StringBuilder();
        if (branchID != null) {
            extraParams.append("&branchID=").append(branchID);
        }
        if (sort != null && !sort.isEmpty()) {
            extraParams.append("&sort=").append(sort);
        }
        model.addAttribute("extraParams", extraParams);
        model.addAttribute("reviews", reviews.getContent());
        model.addAttribute("currentPage", validPage);
        model.addAttribute("totalPages", reviews.getTotalPages());

        model.addAttribute("listBranches", this.branchService.getAllBranches());
        model.addAttribute("branchID", branchID);
        model.addAttribute("sort", sort);
        return "admin/review/show";
    }

    @PostMapping("/admin/review/delete")
    public String postDeleteReview(@RequestParam("reviewID") Long reviewID) {
        this.reviewService.deleteByReviewID(reviewID);
        return "redirect:/admin/review";
    }

}
