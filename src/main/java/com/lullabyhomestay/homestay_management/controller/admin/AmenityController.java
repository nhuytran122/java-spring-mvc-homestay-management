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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.Amenity;
import com.lullabyhomestay.homestay_management.service.AmenityCategoryService;
import com.lullabyhomestay.homestay_management.service.AmenityService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RequestMapping("/admin/amenity")
@PreAuthorize("hasRole('MANAGER')")
@Controller
public class AmenityController {
    private final AmenityCategoryService categoryService;
    private final AmenityService amenityService;

    @PreAuthorize("permitAll()")
    @GetMapping("")
    public String getAmenityPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Long categoryId) {
        int validPage = Math.max(1, page);

        Page<Amenity> amenities = amenityService.searchAmenities(keyword, categoryId, validPage);
        List<Amenity> listAmenities = amenities.getContent();

        StringBuilder extraParams = new StringBuilder();
        if (categoryId != null) {
            extraParams.append("&categoryId=").append(categoryId);
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
        model.addAttribute("categoryId", categoryId);
        return "admin/amenity/show";
    }

    @GetMapping("/create")
    public String getCreateAmenityPage(Model model) {
        model.addAttribute("newAmenity", new Amenity());
        model.addAttribute("listCategories", this.categoryService.getAllAmenityCategories());
        return "admin/amenity/create";
    }

    @PostMapping("/create")
    public String postCreateAmenity(Model model,
            @ModelAttribute("newAmenity") @Valid Amenity amenity,
            BindingResult newAmenityBindingResult) {

        if (newAmenityBindingResult.hasErrors()) {
            model.addAttribute("listCategories", this.categoryService.getAllAmenityCategories());
            return "admin/amenity/create";
        }
        this.amenityService.handleSaveAmenity(amenity);
        return "redirect:/admin/amenity";
    }

    @GetMapping("/update/{id}")
    public String getUpdateAmenityPage(Model model, @PathVariable long id) {
        Amenity amenity = amenityService.getAmenityById(id);

        model.addAttribute("amenity", amenity);
        model.addAttribute("listCategories", this.categoryService.getAllAmenityCategories());
        return "admin/amenity/update";
    }

    @PostMapping("/update")
    public String postUpdateAmenity(Model model,
            @ModelAttribute("amenity") @Valid Amenity amenity,
            BindingResult newAmenityBindingResult) {

        Amenity currentAmenity = this.amenityService.getAmenityById(amenity.getAmenityId());
        if (newAmenityBindingResult.hasErrors()) {
            model.addAttribute("listCategories", this.categoryService.getAllAmenityCategories());
            return "admin/amenity/update";
        }
        currentAmenity.setAmenityName(amenity.getAmenityName());
        currentAmenity.setAmenityCategory(amenity.getAmenityCategory());
        this.amenityService.handleSaveAmenity(currentAmenity);

        return "redirect:/admin/amenity";
    }

    @PostMapping("/delete")
    public String postDeleteAmenity(@RequestParam("amenityId") long amenityId) {
        this.amenityService.deleteByAmenityId(amenityId);
        return "redirect:/admin/amenity";
    }
}
