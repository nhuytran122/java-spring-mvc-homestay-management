package com.lullabyhomestay.homestay_management.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.dto.RegisterDTO;
import com.lullabyhomestay.homestay_management.service.AmenityCategoryService;
import com.lullabyhomestay.homestay_management.service.BranchService;
import com.lullabyhomestay.homestay_management.service.CustomerService;
import com.lullabyhomestay.homestay_management.service.CustomerTypeService;
import com.lullabyhomestay.homestay_management.service.FAQService;
import com.lullabyhomestay.homestay_management.service.HomestayInforService;
import com.lullabyhomestay.homestay_management.service.HomestayServiceService;
import com.lullabyhomestay.homestay_management.service.ReviewService;
import com.lullabyhomestay.homestay_management.service.RoomTypeService;
import com.lullabyhomestay.homestay_management.service.RuleService;
import com.lullabyhomestay.homestay_management.utils.RefundType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class HomePageController {
    private final BranchService branchService;
    private final FAQService faqService;
    private final RoomTypeService roomTypeService;
    private final AmenityCategoryService amenityCategoryService;
    private final RuleService ruleService;
    private final HomestayServiceService service;
    private final HomestayInforService inforService;
    private final CustomerTypeService customerTypeService;
    private final CustomerService customerService;
    private final ReviewService reviewService;

    @GetMapping("/")
    public String getHomePage(Model model,
            @RequestParam(required = false) Long branchID) {
        model.addAttribute("listBranches", branchService.getAllBranches());
        model.addAttribute("listFaqs", faqService.getAllFAQs());
        model.addAttribute("listRoomTypes", roomTypeService.getAllRoomTypes());
        model.addAttribute("listAmenityCategories", amenityCategoryService.getAllAmenityCategories());
        model.addAttribute("listRules", ruleService.getRulesByIsHidden(false));
        model.addAttribute("listServices", service.getAllServices());
        model.addAttribute("listInfors", inforService.getAllInforHomestay());
        model.addAttribute("listCustomerTypes", customerTypeService.getAllCustomerTypes());
        model.addAttribute("listReviews", reviewService.getAllFiveStarReviews());
        model.addAttribute("refundTypes", RefundType.values());
        return "client/homepage/show";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());
        return "client/auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("registerUser") @Valid RegisterDTO registerDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            return "client/auth/register";
        }
        this.customerService.handleRegisterAccount(registerDTO);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session.getAttribute("id") != null) {
            return "redirect:/";
        }
        return "client/auth/login";
    }

    @GetMapping("/access-deny")
    public String getDenyPage(Model model) {
        return "client/auth/deny";
    }
}
