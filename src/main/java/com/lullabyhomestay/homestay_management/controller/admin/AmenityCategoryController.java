package com.lullabyhomestay.homestay_management.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.lullabyhomestay.homestay_management.domain.AmenityCategory;
import com.lullabyhomestay.homestay_management.service.AmenityCategoryService;
import com.lullabyhomestay.homestay_management.service.IconService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;

@Controller
@AllArgsConstructor
@PreAuthorize("hasRole('MANAGER')")
@RequestMapping("/admin/amenity-category")
public class AmenityCategoryController {
    private final AmenityCategoryService amenityCategoryService;
    private final IconService iconService;

    @PreAuthorize("permitAll()")
    @GetMapping("")
    public String getAmenityCategoryPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword) {
        int validPage = Math.max(1, page);

        Page<AmenityCategory> categories = amenityCategoryService.searchAmenityCategories(keyword, validPage);

        List<AmenityCategory> listAmenityCategories = categories.getContent();
        model.addAttribute("categories", listAmenityCategories);

        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", validPage);
        model.addAttribute("totalPages", categories.getTotalPages());
        return "admin/amenity-category/show";
    }

    @GetMapping("/create")
    public String getCreateAmenityCategoryPage(Model model) {
        model.addAttribute("newCategory", new AmenityCategory());
        model.addAttribute("iconList", iconService.getCachedIconList());
        return "admin/amenity-category/create";
    }

    @PostMapping("/create")
    public String postCreateAmenityCategory(Model model,
            @ModelAttribute("newCategory") @Valid AmenityCategory amenityCategory,
            BindingResult newCategoryBindingResult) {
        if (amenityCategoryService.existsByName(amenityCategory.getCategoryName())) {
            newCategoryBindingResult.rejectValue("categoryName", "error.categoryName",
                    "Tên phân loại tiện nghi đã tồn tại!");
        }
        if (newCategoryBindingResult.hasErrors()) {
            model.addAttribute("iconList", iconService.getCachedIconList());
            return "admin/amenity-category/create";
        }

        amenityCategoryService.handleSaveAmenityCategory(amenityCategory);
        return "redirect:/admin/amenity-category";
    }

    @GetMapping("/update/{id}")
    public String getUpdateCategoryPage(Model model, @PathVariable long id) {
        AmenityCategory category = amenityCategoryService.getAmenityCategoryByID(id);

        model.addAttribute("category", category);
        model.addAttribute("iconList", iconService.getCachedIconList());
        return "admin/amenity-category/update";
    }

    @PostMapping("/update")
    public String postUpdateCategory(Model model,
            @ModelAttribute("category") @Valid AmenityCategory category,
            BindingResult newCategoryBindingResult,
            HttpServletRequest request) {

        AmenityCategory currentCategory = this.amenityCategoryService.getAmenityCategoryByID(category.getCategoryId());

        if (amenityCategoryService.existsByNameAndNotId(currentCategory.getCategoryName(),
                currentCategory.getCategoryId())) {
            newCategoryBindingResult.rejectValue("categoryName", "error.categoryName",
                    "Tên phân loại tiện nghi đã tồn tại!");
        }
        if (newCategoryBindingResult.hasErrors()) {
            model.addAttribute("iconList", iconService.getCachedIconList());
            return "admin/amenity-category/update";
        }

        currentCategory.setCategoryName(category.getCategoryName());
        currentCategory.setIcon(category.getIcon());
        currentCategory.setDescription(category.getDescription());

        this.amenityCategoryService.handleSaveAmenityCategory(currentCategory);

        return "redirect:/admin/amenity-category";
    }

    @GetMapping("/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteCategory(@PathVariable long id) {
        boolean canDelete = amenityCategoryService.canDeleteCategory(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/delete")
    public String postDeleteBranch(@RequestParam("categoryID") long categoryID) {
        this.amenityCategoryService.deleteByCategoryID(categoryID);
        return "redirect:/admin/amenity-category";
    }

}