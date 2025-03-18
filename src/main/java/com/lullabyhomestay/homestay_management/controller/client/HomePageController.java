package com.lullabyhomestay.homestay_management.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.service.AmenityCategoryService;
import com.lullabyhomestay.homestay_management.service.BranchService;
import com.lullabyhomestay.homestay_management.service.CustomerTypeService;
import com.lullabyhomestay.homestay_management.service.FAQService;
import com.lullabyhomestay.homestay_management.service.HomestayInforService;
import com.lullabyhomestay.homestay_management.service.HomestayServiceService;
import com.lullabyhomestay.homestay_management.service.RoomService;
import com.lullabyhomestay.homestay_management.service.RoomTypeService;
import com.lullabyhomestay.homestay_management.service.RuleService;

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
        // Pageable pageable = PageRequest.of(0, 10);
        // Page<Product> prs = this.productService.fetchProducts(pageable);
        // List<Product> products = prs.getContent();
        // model.addAttribute("products", products);
        return "client/homepage/show";
    }
}
