package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.CustomerType;
import com.lullabyhomestay.homestay_management.service.CustomerTypeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class CustomerTypeController {
    private final CustomerTypeService customerTypeService;

    @GetMapping("/admin/customer/customer-type")
    public String getAmenityCategoryPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword) {
        int validPage = Math.max(1, page);

        Page<CustomerType> types = customerTypeService.searchCustomerTypes(keyword, validPage);

        List<CustomerType> listTypes = types.getContent();
        model.addAttribute("listTypes", listTypes);

        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", validPage);
        model.addAttribute("totalPages", types.getTotalPages());
        return "admin/customer-type/show";
    }

    @GetMapping("/admin/customer/customer-type/create")
    public String getCreateCustomerType(Model model) {
        model.addAttribute("newType", new CustomerType());
        return "admin/customer-type/create";
    }

    @PostMapping("/admin/customer/customer-type/create")
    public String postCreateCustomerType(Model model,
            @ModelAttribute("newType") @Valid CustomerType customerType,
            BindingResult result,
            HttpServletRequest request) {
        // HttpSession session = request.getSession(false);
        if (result.hasErrors()) {
            return "admin/customer-type/create";
        }
        customerTypeService.handleSavaCustomerType(customerType);
        return "redirect:/admin/customer/customer-type";
    }

    @GetMapping("/admin/customer/customer-type/update/{id}")
    public String getUpdateTypePage(Model model, @PathVariable long id) {
        CustomerType type = customerTypeService.getCustomerTypeByID(id);

        model.addAttribute("type", type);
        return "admin/customer-type/update";
    }

    @PostMapping("/admin/customer/customer-type/update")
    public String postUpdateType(Model model,
            @ModelAttribute("type") @Valid CustomerType type,
            BindingResult result,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        CustomerType currentType = customerTypeService.getCustomerTypeByID(type.getCustomerTypeID());
        if (result.hasErrors()) {
            return "admin/customer-type/update";
        }
        currentType.setName(type.getName());
        currentType.setMinPoint(type.getMinPoint());
        currentType.setDiscountRate(type.getDiscountRate());
        currentType.setDescription(type.getDescription());

        customerTypeService.handleSavaCustomerType(currentType);
        return "redirect:/admin/customer/customer-type";
    }

    @GetMapping("/admin/customer/customer-type/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteType(@PathVariable Long id) {
        boolean canDelete = customerTypeService.canDeleteType(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/admin/customer/customer-type/delete")
    public String postDeleteType(@RequestParam("customerTypeID") Long typeID) {
        this.customerTypeService.deleteByCustomerTypeID(typeID);
        return "redirect:/admin/customer/customer-type";
    }

}
