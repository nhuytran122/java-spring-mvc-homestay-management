package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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

import com.lullabyhomestay.homestay_management.domain.InventoryCategory;
import com.lullabyhomestay.homestay_management.service.InventoryCategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
@RequestMapping("/admin/inventory-category")
public class InventoryCategoryController {
    private final InventoryCategoryService inventoryCategoryService;

    @GetMapping("")
    public String getInventoryCategoryPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword) {
        int validPage = Math.max(1, page);

        Page<InventoryCategory> categories = inventoryCategoryService.searchInventoryCategories(keyword, validPage);

        List<InventoryCategory> listInventoryCategories = categories.getContent();
        model.addAttribute("categories", listInventoryCategories);

        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", validPage);
        model.addAttribute("totalPages", categories.getTotalPages());
        return "admin/inventory-category/show";
    }

    @GetMapping("/create")
    public String getCreateInventoryCategoryPage(Model model) {
        model.addAttribute("newCategory", new InventoryCategory());
        return "admin/inventory-category/create";
    }

    @PostMapping("/create")
    public String postCreateInventoryCategory(Model model,
            @ModelAttribute("newCategory") @Valid InventoryCategory inventoryCategory,
            BindingResult newCategoryBindingResult,
            HttpServletRequest request) {
        if (inventoryCategoryService.existsByNameAndNotId(inventoryCategory.getCategoryName(),
                inventoryCategory.getCategoryId())) {
            newCategoryBindingResult.rejectValue("categoryName", "error.categoryName",
                    "Tên phân loại đồ dùng đã tồn tại!");
        }

        if (newCategoryBindingResult.hasErrors()) {
            return "admin/inventory-category/create";
        }
        inventoryCategoryService.handleSaveInventoryCategory(inventoryCategory);
        return "redirect:/admin/inventory-category";
    }

    @GetMapping("/update/{id}")
    public String getUpdateCategoryPage(Model model, @PathVariable long id) {
        InventoryCategory category = inventoryCategoryService.getInventoryCategoryById(id);

        model.addAttribute("category", category);
        return "admin/inventory-category/update";
    }

    @PostMapping("/update")
    public String postUpdateBranch(Model model,
            @ModelAttribute("category") @Valid InventoryCategory category,
            BindingResult newCategoryBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

        InventoryCategory currentCategory = this.inventoryCategoryService
                .getInventoryCategoryById(category.getCategoryId());

        if (inventoryCategoryService.existsByNameAndNotId(category.getCategoryName(), category.getCategoryId())) {
            newCategoryBindingResult.rejectValue("categoryName", "error.categoryName",
                    "Tên phân loại đồ dùng đã tồn tại!");
        }
        if (newCategoryBindingResult.hasErrors()) {
            return "admin/inventory-category/update";
        }

        currentCategory.setCategoryName(category.getCategoryName());
        currentCategory.setDescription(category.getDescription());
        this.inventoryCategoryService.handleSaveInventoryCategory(currentCategory);
        return "redirect:/admin/inventory-category";
    }

    @GetMapping("/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteCategory(@PathVariable long id) {
        boolean canDelete = inventoryCategoryService.canDeleteCategory(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/delete")
    public String postDeleteBranch(@RequestParam("categoryId") long categoryId) {
        this.inventoryCategoryService.deleteByCategoryId(categoryId);
        return "redirect:/admin/inventory-category";
    }
}
