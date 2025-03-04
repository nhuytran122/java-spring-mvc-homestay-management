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
import java.util.Optional;

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

        if (newCategoryBindingResult.hasErrors()) {
            model.addAttribute("iconList", iconService.getCachedIconList());
            return "admin/amenity-category/create";
        }

        amenityCategoryService.handleSaveAmenityCategory(amenityCategory);
        return "redirect:/admin/amenity-category";
    }

    @GetMapping("/admin/amenity-category/update/{id}")
    public String getUpdateCategoryPage(Model model, @PathVariable long id) {
        Optional<AmenityCategory> category = amenityCategoryService.getAmenityCategoryByID(id);
        if (!category.isPresent()) {
            return "admin/amenity-category";
        }

        model.addAttribute("category", category.get());
        model.addAttribute("iconList", iconService.getCachedIconList());
        return "admin/amenity-category/update";
    }

    @PostMapping("/admin/amenity-category/update")
    public String postUpdateBranch(Model model,
            @ModelAttribute("category") @Valid AmenityCategory category,
            BindingResult newCategoryBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

        AmenityCategory currentCategory = this.amenityCategoryService.getAmenityCategoryByID(category.getCategoryID())
                .get();

        if (newCategoryBindingResult.hasErrors()) {
            model.addAttribute("iconList", iconService.getCachedIconList());
            return "admin/amenity-category/update";
        }

        if (currentCategory != null) {
            currentCategory.setCategoryName(category.getCategoryName());
            currentCategory.setIcon(category.getIcon());
            currentCategory.setDescription(category.getDescription());

            this.amenityCategoryService.handleSaveAmenityCategory(currentCategory);
        }
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