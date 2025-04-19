package com.lullabyhomestay.homestay_management.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
public class AmenityCategoryController {
    private final AmenityCategoryService amenityCategoryService;
    private final IconService iconService;

    @GetMapping("/admin/amenity-category")
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

    @GetMapping("/admin/amenity-category/create")
    public String getCreateAmenityCategoryPage(Model model) {
        model.addAttribute("newCategory", new AmenityCategory());
        model.addAttribute("iconList", iconService.getCachedIconList());
        return "admin/amenity-category/create";
    }

    @PostMapping("/admin/amenity-category/create")
    public String postCreateAmenityCategory(Model model,
            @ModelAttribute("newCategory") @Valid AmenityCategory amenityCategory,
            BindingResult newCategoryBindingResult,
            HttpServletRequest request) {
        // HttpSession session = request.getSession(false);
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

    @GetMapping("/admin/amenity-category/update/{id}")
    public String getUpdateCategoryPage(Model model, @PathVariable long id) {
        AmenityCategory category = amenityCategoryService.getAmenityCategoryByID(id);

        model.addAttribute("category", category);
        model.addAttribute("iconList", iconService.getCachedIconList());
        return "admin/amenity-category/update";
    }

    @PostMapping("/admin/amenity-category/update")
    public String postUpdateCategory(Model model,
            @ModelAttribute("category") @Valid AmenityCategory category,
            BindingResult newCategoryBindingResult,
            HttpServletRequest request) {

        AmenityCategory currentCategory = this.amenityCategoryService.getAmenityCategoryByID(category.getCategoryID());

        if (amenityCategoryService.existsByNameAndNotId(currentCategory.getCategoryName(),
                currentCategory.getCategoryID())) {
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

    @GetMapping("/admin/amenity-category/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteCategory(@PathVariable long id) {
        boolean canDelete = amenityCategoryService.canDeleteCategory(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/admin/amenity-category/delete")
    public String postDeleteBranch(@RequestParam("categoryID") long categoryID) {
        this.amenityCategoryService.deleteByCategoryID(categoryID);
        return "redirect:/admin/amenity-category";
    }

}