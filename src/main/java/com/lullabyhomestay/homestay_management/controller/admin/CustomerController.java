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

import com.lullabyhomestay.homestay_management.domain.CustomerType;
import com.lullabyhomestay.homestay_management.domain.dto.CustomerDTO;
import com.lullabyhomestay.homestay_management.domain.dto.SearchCustomerCriterialDTO;
import com.lullabyhomestay.homestay_management.service.CustomerService;
import com.lullabyhomestay.homestay_management.service.CustomerTypeService;
import com.lullabyhomestay.homestay_management.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
@RequestMapping("/admin/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerTypeService customerTypeService;
    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
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

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public String getDetailCustomerPage(Model model, @PathVariable long id) {
        CustomerDTO customerDTO = customerService.getCustomerDTOById(id);
        model.addAttribute("customer", customerDTO);
        return "admin/customer/detail";
    }

    @GetMapping("/create")
    public String getCreateCustomerPage(Model model) {
        model.addAttribute("newCustomer", new CustomerDTO());
        return "admin/customer/create";
    }

    @PostMapping("/create")
    public String postCreateCustomer(Model model,
            @ModelAttribute("newCustomer") @Valid CustomerDTO customer,
            BindingResult result) {
        if (result.hasErrors()) {
            return "admin/customer/create";
        }
        this.customerService.handleCreateCustomer(customer);
        return "redirect:/admin/customer";
    }

    @GetMapping("/update/{id}")
    public String getUpdateCustomerPage(Model model, @PathVariable long id) {
        CustomerDTO customer = customerService.getCustomerDTOById(id);
        model.addAttribute("customer", customer);
        return "admin/customer/update";
    }

    @PostMapping("/update")
    public String postUpdateCustomer(Model model,
            @ModelAttribute("customer") @Valid CustomerDTO customerDTO,
            BindingResult result) {

        CustomerDTO customer = (customerService.getCustomerDTOById(customerDTO.getCustomerId()));
        if (result.hasErrors()) {
            return "admin/customer/update";
        }
        this.userService.updateProfile(customer.getUserId(), customerDTO);
        return "redirect:/admin/customer";
    }

    @GetMapping("/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteCustomer(@PathVariable long id) {
        boolean canDelete = customerService.canDeleteCustomer(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/delete")
    public String postDeleteCustomer(@RequestParam("customerId") long customerId) {
        this.customerService.deleteByCustomerId(customerId);
        return "redirect:/admin/customer";
    }
}
