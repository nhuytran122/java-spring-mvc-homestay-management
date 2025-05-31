package com.lullabyhomestay.homestay_management.controller.admin;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

import com.lullabyhomestay.homestay_management.domain.InventoryItem;
import com.lullabyhomestay.homestay_management.service.InventoryItemService;
import com.lullabyhomestay.homestay_management.service.InventoryCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
@RequestMapping("/admin/inventory-item")
public class InventoryItemController {
    private final InventoryItemService itemService;
    private final InventoryCategoryService categoryService;

    @GetMapping("")
    public String getInventoryItemPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "") String sort) {
        int validPage = Math.max(1, page);
        Page<InventoryItem> items = itemService.searchItems(keyword, categoryId, validPage, sort);
        List<InventoryItem> listItems = items.getContent();

        StringBuilder extraParams = new StringBuilder();
        if (categoryId != null) {
            extraParams.append("&categoryId=").append(categoryId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            extraParams.append("&keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8));
        }
        if (sort != null && !sort.isEmpty()) {
            extraParams.append("&sort=").append(sort);
        }
        model.addAttribute("extraParams", extraParams);
        model.addAttribute("items", listItems);
        model.addAttribute("currentPage", validPage);
        model.addAttribute("totalPages", items.getTotalPages());

        model.addAttribute("listCategories", this.categoryService.getAllInventoryCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sort", sort);
        return "admin/inventory-item/show";
    }

    @GetMapping("/create")
    public String getCreateInventoryItemPage(Model model) {
        model.addAttribute("newItem", new InventoryItem());
        model.addAttribute("listCategories", this.categoryService.getAllInventoryCategories());
        return "admin/inventory-item/create";
    }

    @PostMapping("/create")
    public String postCreateInventoryItem(Model model,
            @ModelAttribute("newItem") @Valid InventoryItem item,
            BindingResult newInventoryItemBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

        if (newInventoryItemBindingResult.hasErrors()) {
            model.addAttribute("listCategories", this.categoryService.getAllInventoryCategories());
            return "admin/inventory-item/create";
        }
        this.itemService.handleSaveInventoryItem(item);
        return "redirect:/admin/inventory-item";
    }

    @GetMapping("/update/{id}")
    public String getUpdateInventoryItemPage(Model model, @PathVariable long id) {
        InventoryItem item = itemService.getInventoryItemById(id);

        model.addAttribute("item", item);
        model.addAttribute("listCategories", this.categoryService.getAllInventoryCategories());
        return "admin/inventory-item/update";
    }

    @PostMapping("/update")
    public String postUpdateInventoryItem(Model model,
            @ModelAttribute("item") @Valid InventoryItem item,
            BindingResult newInventoryItemBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        InventoryItem currentItem = this.itemService.getInventoryItemById(item.getItemId());
        if (newInventoryItemBindingResult.hasErrors()) {
            model.addAttribute("listCategories", this.categoryService.getAllInventoryCategories());
            return "admin/inventory-item/update";
        }

        currentItem.setItemName(item.getItemName());
        currentItem.setInventoryCategory(item.getInventoryCategory());
        currentItem.setPrice(item.getPrice());
        currentItem.setUnit(item.getUnit());
        this.itemService.handleSaveInventoryItem(currentItem);
        return "redirect:/admin/inventory-item";
    }

    @GetMapping("/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteItem(@PathVariable long id) {
        boolean canDelete = itemService.canDeleteItem(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/delete")
    public String postDeleteInventoryItem(@RequestParam("itemId") long itemId) {
        this.itemService.deleteByInventoryItemId(itemId);
        return "redirect:/admin/inventory-item";
    }
}
