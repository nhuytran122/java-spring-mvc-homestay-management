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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final BranchService branchService;

    @GetMapping("")
    public String getReviewsPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String sort,
            @RequestParam(required = false) Long branchId) {
        int validPage = Math.max(1, page);

        Page<Review> reviews = reviewService.searchReviews(branchId, sort, validPage);

        StringBuilder extraParams = new StringBuilder();
        if (branchId != null) {
            extraParams.append("&branchId=").append(branchId);
        }
        if (sort != null && !sort.isEmpty()) {
            extraParams.append("&sort=").append(sort);
        }
        model.addAttribute("extraParams", extraParams);
        model.addAttribute("reviews", reviews.getContent());
        model.addAttribute("currentPage", validPage);
        model.addAttribute("totalPages", reviews.getTotalPages());

        model.addAttribute("listBranches", this.branchService.getAllBranches());
        model.addAttribute("branchId", branchId);
        model.addAttribute("sort", sort);
        return "admin/review/show";
    }

    @PostMapping("/delete")
    public String postDeleteReview(@RequestParam("reviewId") Long reviewId) {
        this.reviewService.deleteByReviewId(reviewId);
        return "redirect:/admin/review";
    }

}
