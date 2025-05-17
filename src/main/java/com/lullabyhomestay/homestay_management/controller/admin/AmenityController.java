package com.lullabyhomestay.homestay_management.controller.admin;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.Amenity;
import com.lullabyhomestay.homestay_management.service.AmenityCategoryService;
import com.lullabyhomestay.homestay_management.service.AmenityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class AmenityController {
    private final AmenityCategoryService categoryService;
    private final AmenityService amenityService;

    @GetMapping("/admin/amenity")
    public String getAmenityPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Long categoryID) {
        int validPage = Math.max(1, page);

        Page<Amenity> amenities = amenityService.searchAmenities(keyword, categoryID, validPage);
        List<Amenity> listAmenities = amenities.getContent();

        StringBuilder extraParams = new StringBuilder();
        if (categoryID != null) {
            extraParams.append("&categoryID=").append(categoryID);
        }
        if (keyword != null && !keyword.isEmpty()) {
            extraParams.append("&keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8));
        }
        model.addAttribute("extraParams", extraParams);
        model.addAttribute("amenities", listAmenities);
        model.addAttribute("currentPage", validPage);
        model.addAttribute("totalPages", amenities.getTotalPages());

        model.addAttribute("listCategories", this.categoryService.getAllAmenityCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryID", categoryID);
        return "admin/amenity/show";
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/admin/amenity/create")
    public String getCreateAmenityPage(Model model) {
        model.addAttribute("newAmenity", new Amenity());
        model.addAttribute("listCategories", this.categoryService.getAllAmenityCategories());
        return "admin/amenity/create";
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @PostMapping("/admin/amenity/create")
    public String postCreateAmenity(Model model,
            @ModelAttribute("newAmenity") @Valid Amenity amenity,
            BindingResult newAmenityBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

        if (newAmenityBindingResult.hasErrors()) {
            model.addAttribute("listCategories", this.categoryService.getAllAmenityCategories());
            return "admin/amenity/create";
        }
        this.amenityService.handleSaveAmenity(amenity);
        return "redirect:/admin/amenity";
    }

    @GetMapping("/admin/amenity/update/{id}")
    public String getUpdateAmenityPage(Model model, @PathVariable long id) {
        Amenity amenity = amenityService.getAmenityByID(id);

        model.addAttribute("amenity", amenity);
        model.addAttribute("listCategories", this.categoryService.getAllAmenityCategories());
        return "admin/amenity/update";
    }

    @PostMapping("/admin/amenity/update")
    public String postUpdateAmenity(Model model,
            @ModelAttribute("amenity") @Valid Amenity amenity,
            BindingResult newAmenityBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        Amenity currentAmenity = this.amenityService.getAmenityByID(amenity.getAmenityID());
        if (newAmenityBindingResult.hasErrors()) {
            model.addAttribute("listCategories", this.categoryService.getAllAmenityCategories());
            return "admin/amenity/update";
        }
        currentAmenity.setAmenityName(amenity.getAmenityName());
        currentAmenity.setAmenityCategory(amenity.getAmenityCategory());
        this.amenityService.handleSaveAmenity(currentAmenity);

        return "redirect:/admin/amenity";
    }

    @PostMapping("/admin/amenity/delete")
    public String postDeleteAmenity(@RequestParam("amenityID") long amenityID) {
        this.amenityService.deleteByAmenityID(amenityID);
        return "redirect:/admin/amenity";
    }
}
