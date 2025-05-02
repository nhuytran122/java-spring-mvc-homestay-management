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
import com.lullabyhomestay.homestay_management.domain.dto.CustomerDTO;
import com.lullabyhomestay.homestay_management.domain.dto.SearchCustomerCriterialDTO;
import com.lullabyhomestay.homestay_management.service.CustomerService;
import com.lullabyhomestay.homestay_management.service.CustomerTypeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerTypeService customerTypeService;

    @GetMapping("/admin/customer")
    public String getCustomerPage(
            Model model,
            @RequestParam(defaultValue = "1") int page,
            @ModelAttribute SearchCustomerCriterialDTO criteria) {

        Page<CustomerDTO> customers = customerService.searchCustomers(criteria, page);
        List<CustomerDTO> listCustomers = customers.getContent();

        List<CustomerType> listTypes = customerTypeService.getAllCustomerTypes();
        model.addAttribute("listTypes", listTypes);
        model.addAttribute("criteria", criteria);
        model.addAttribute("extraParams", criteria.convertToExtraParams());

        model.addAttribute("listCustomers", listCustomers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", customers.getTotalPages());
        return "admin/customer/show";
    }

    @GetMapping("/admin/customer/create")
    public String getCreateCustomerPage(Model model) {
        model.addAttribute("newCustomer", new CustomerDTO());
        return "admin/customer/create";
    }

    @PostMapping("/admin/customer/create")
    public String postCreateCustomer(Model model,
            @ModelAttribute("newCustomer") @Valid CustomerDTO customer,
            BindingResult result,
            HttpServletRequest request) {
        // HttpSession session = request.getSession(false);
        if (result.hasErrors()) {
            return "admin/customer/create";
        }
        this.customerService.handleSaveCustomer(customer);
        return "redirect:/admin/customer";
    }

    @GetMapping("/admin/customer/update/{id}")
    public String getUpdateCustomerPage(Model model, @PathVariable long id) {
        CustomerDTO customer = customerService.getCustomerDTOByID(id);
        model.addAttribute("customer", customer);
        return "admin/customer/update";
    }

    @PostMapping("/admin/customer/update")
    public String postUpdateCustomer(Model model,
            @ModelAttribute("customer") @Valid CustomerDTO customer,
            BindingResult result,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        CustomerDTO currentCustomer = (customerService.getCustomerDTOByID(customer.getCustomerID()));
        if (result.hasErrors()) {
            return "admin/customer/update";
        }
        currentCustomer.setFullName(customer.getFullName());
        currentCustomer.setAddress(customer.getAddress());
        currentCustomer.setPhone(customer.getPhone());

        this.customerService.handleSaveCustomer(currentCustomer);
        return "redirect:/admin/customer";
    }

    @GetMapping("/admin/customer/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteCustomer(@PathVariable long id) {
        boolean canDelete = customerService.canDeleteCustomer(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/admin/customer/delete")
    public String postDeleteCustomer(@RequestParam("customerID") long customerID) {
        this.customerService.deleteByCustomerID(customerID);
        return "redirect:/admin/customer";
    }
}
