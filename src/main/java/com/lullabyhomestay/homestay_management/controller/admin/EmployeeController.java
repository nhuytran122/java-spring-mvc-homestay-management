package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lullabyhomestay.homestay_management.domain.Role;
import com.lullabyhomestay.homestay_management.domain.dto.EmployeeDTO;
import com.lullabyhomestay.homestay_management.domain.dto.SearchEmployeeCriterialDTO;
import com.lullabyhomestay.homestay_management.service.EmployeeService;
import com.lullabyhomestay.homestay_management.service.RoleService;
import com.lullabyhomestay.homestay_management.service.UploadService;
import com.lullabyhomestay.homestay_management.service.validator.AdminValidation;
import com.lullabyhomestay.homestay_management.utils.SystemRole;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class EmployeeController {
    private final EmployeeService employeeService;
    private final RoleService roleService;
    private final UploadService uploadService;

    @GetMapping("/admin/employee")
    public String getEmployeePage(
            Model model,
            @RequestParam(defaultValue = "1") int page,
            @ModelAttribute SearchEmployeeCriterialDTO criteria) {

        Page<EmployeeDTO> employees = employeeService.searchEmployees(criteria, page);
        List<EmployeeDTO> listEmployees = employees.getContent();

        model.addAttribute("listRoles", getAllNonCustomerRoles());
        model.addAttribute("criteria", criteria);
        model.addAttribute("extraParams", criteria.convertToExtraParams());

        model.addAttribute("listEmployees", listEmployees);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employees.getTotalPages());
        return "admin/employee/show";
    }

    @GetMapping("/admin/employee/{id}")
    public String getDetailEmployeePage(Model model, @PathVariable long id) {
        EmployeeDTO employee = employeeService.getEmployeeDTOByID(id);
        model.addAttribute("employee", employee);
        return "admin/employee/detail";
    }

    @GetMapping("/admin/employee/create")
    public String getCreateEmployeePage(Model model) {
        model.addAttribute("listRoles", getAllNonCustomerRoles());
        model.addAttribute("newEmployee", new EmployeeDTO());
        return "admin/employee/create";
    }

    @PostMapping("/admin/employee/create")
    public String postCreateEmployee(Model model,
            @ModelAttribute("newEmployee") @Valid EmployeeDTO employee,
            BindingResult newEmployeeBindingResult,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        if (newEmployeeBindingResult.hasErrors()) {
            model.addAttribute("listRoles", getAllNonCustomerRoles());
            return "admin/employee/create";
        }
        String img;
        if (!file.isEmpty()) {
            img = this.uploadService.handleSaveUploadFile(file, "avatar");
            employee.setAvatar(img);
        }
        this.employeeService.handleCreateEmployee(employee);
        return "redirect:/admin/employee";
    }

    @GetMapping("/admin/employee/update/{id}")
    public String getUpdateEmployeePage(Model model, @PathVariable long id) {
        EmployeeDTO employee = employeeService.getEmployeeDTOByID(id);
        model.addAttribute("listRoles", getAllNonCustomerRoles());

        model.addAttribute("employee", employee);
        return "admin/employee/update";
    }

    @PostMapping("/admin/employee/update")
    public String postUpdateEmployee(Model model,
            @ModelAttribute("employee") @Validated(AdminValidation.class) @Valid EmployeeDTO employee,
            BindingResult newEmployeeBindingResult,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        EmployeeDTO currentEmployee = (employeeService.getEmployeeDTOByID(employee.getEmployeeID()));
        if (newEmployeeBindingResult.hasErrors()) {
            model.addAttribute("listRoles", getAllNonCustomerRoles());
            return "admin/employee/update";
        }
        if (!file.isEmpty()) {
            String img = this.uploadService.handleSaveUploadFile(file, "avatar");
            currentEmployee.setAvatar(img);
        }
        currentEmployee.setFullName(employee.getFullName());
        currentEmployee.setEmail(employee.getEmail());
        currentEmployee.setAddress(employee.getAddress());
        currentEmployee.setPhone(employee.getPhone());
        currentEmployee.setRole(employee.getRole());
        currentEmployee.setSalary(employee.getSalary());

        this.employeeService.handleUpdateEmployee(currentEmployee);
        return "redirect:/admin/employee";
    }

    @GetMapping("/admin/employee/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteEmployee(@PathVariable long id) {
        boolean canDelete = employeeService.canDeleteEmployee(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/admin/employee/delete")
    public String postDeleteEmployee(@RequestParam("employeeID") long employeeID) {
        this.employeeService.deleteByEmployeeID(employeeID);
        return "redirect:/admin/employee";
    }

    public List<Role> getAllNonCustomerRoles() {
        return roleService.getAllRoles()
                .stream()
                .filter(role -> role.getRoleName() != SystemRole.CUSTOMER)
                .collect(Collectors.toList());
    }

}