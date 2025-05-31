package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.groups.Default;
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
@PreAuthorize("hasRole('MANAGER')")
@RequestMapping("/admin/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final RoleService roleService;
    private final UploadService uploadService;

    @GetMapping("")
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

    @GetMapping("/{id}")
    public String getDetailEmployeePage(Model model, @PathVariable long id) {
        EmployeeDTO employee = employeeService.getEmployeeDTOById(id);
        model.addAttribute("employee", employee);
        return "admin/employee/detail";
    }

    @GetMapping("/create")
    public String getCreateEmployeePage(Model model) {
        model.addAttribute("listRoles", getAllNonCustomerRoles());
        model.addAttribute("newEmployee", new EmployeeDTO());
        return "admin/employee/create";
    }

    @PostMapping("/create")
    public String postCreateEmployee(Model model,
            @Validated({ Default.class, AdminValidation.class }) @ModelAttribute("newEmployee") EmployeeDTO employee,
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

    @GetMapping("/update/{id}")
    public String getUpdateEmployeePage(Model model, @PathVariable long id) {
        EmployeeDTO employee = employeeService.getEmployeeDTOById(id);
        model.addAttribute("listRoles", getAllNonCustomerRoles());

        model.addAttribute("employee", employee);
        return "admin/employee/update";
    }

    @PostMapping("/update")
    public String postUpdateEmployee(Model model,
            @ModelAttribute("employee") @Validated(AdminValidation.class) @Valid EmployeeDTO employee,
            BindingResult newEmployeeBindingResult,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        EmployeeDTO currentEmployee = (employeeService.getEmployeeDTOById(employee.getEmployeeId()));
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

    @GetMapping("/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteEmployee(@PathVariable long id) {
        boolean canDelete = employeeService.canDeleteEmployee(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/delete")
    public String postDeleteEmployee(@RequestParam("employeeId") long employeeId) {
        this.employeeService.deleteByEmployeeId(employeeId);
        return "redirect:/admin/employee";
    }

    public List<Role> getAllNonCustomerRoles() {
        return roleService.getAllRoles()
                .stream()
                .filter(role -> role.getRoleName() != SystemRole.CUSTOMER)
                .collect(Collectors.toList());
    }

}