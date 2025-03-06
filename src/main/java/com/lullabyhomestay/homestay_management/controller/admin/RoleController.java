package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.Role;
import com.lullabyhomestay.homestay_management.service.RoleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@AllArgsConstructor
@Controller
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/admin/employee-role")
    public String getRolePage(Model model) {
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("listRoles", roles);
        return "admin/role/show";
    }

    @GetMapping("/admin/employee-role/create")
    public String getCreateRolePage(Model model) {
        model.addAttribute("newRole", new Role());
        return "admin/role/create";
    }

    @PostMapping("/admin/employee-role/create")
    public String postCreateRole(Model model,
            @ModelAttribute("newRole") @Valid Role role,
            BindingResult newRoleBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        if (newRoleBindingResult.hasErrors()) {
            return "admin/role/create";
        }
        roleService.handleSaveRole(role);
        return "redirect:/admin/employee-role";
    }

    @GetMapping("/admin/employee-role/update/{id}")
    public String getUpdateRolePage(Model model, @PathVariable long id) {
        Role role = roleService.getRoleByRoleID(id);
        model.addAttribute("role", role);
        return "admin/role/update";
    }

    @PostMapping("/admin/employee-role/update")
    public String postUpdateRole(Model model,
            @ModelAttribute("role") @Valid Role role,
            BindingResult roleBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        Role currentRole = roleService.getRoleByRoleID(role.getRoleID());
        if (roleBindingResult.hasErrors()) {
            return "admin/role/update";
        }
        currentRole.setRoleName(role.getRoleName());
        roleService.handleSaveRole(currentRole);
        return "redirect:/admin/employee-role";
    }

    @GetMapping("/admin/employee-role/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteRole(@PathVariable long id) {
        boolean canDelete = roleService.canDeleteRole(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/admin/employee-role/delete")
    public String postDeleteRole(@RequestParam("roleID") long roleID) {
        roleService.deleteByRoleID(roleID);
        return "redirect:/admin/employee-role";
    }
}
