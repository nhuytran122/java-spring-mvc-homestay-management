package com.lullabyhomestay.homestay_management.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.lullabyhomestay.homestay_management.domain.dto.EmployeeDTO;
import com.lullabyhomestay.homestay_management.domain.dto.PasswordChangeDTO;
import com.lullabyhomestay.homestay_management.service.EmployeeService;
import com.lullabyhomestay.homestay_management.service.UploadService;
import com.lullabyhomestay.homestay_management.utils.AuthUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Controller
public class AdminUserProfileController {
    private final EmployeeService employeeService;
    private final UploadService uploadService;

    @GetMapping("/admin/profile")
    public String getProfilePage(Model model) {
        EmployeeDTO employeeDTO = AuthUtils.getLoggedInEmployee(employeeService);
        model.addAttribute("employee", employeeDTO);
        return "admin/profile/show";
    }

    @GetMapping("/admin/profile/update")
    public String getUpdateProfilePage(Model model) {
        EmployeeDTO employeeDTO = AuthUtils.getLoggedInEmployee(employeeService);
        model.addAttribute("employee", employeeDTO);
        return "admin/profile/update";
    }

    @PostMapping("/admin/profile/update")
    public String postUpdateProfile(@ModelAttribute("employee") @Valid EmployeeDTO employeeDTO,
            BindingResult result,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        EmployeeDTO currentEmployeeDTO = AuthUtils.getLoggedInEmployee(employeeService);
        if (result.hasErrors())
            return "admin/profile/update";
        String img;
        if (!file.isEmpty()) {
            img = this.uploadService.handleSaveUploadFile(file, "avatar");
            employeeDTO.setAvatar(img);
        }
        employeeService.updateProfile(currentEmployeeDTO.getEmployeeID(), employeeDTO);
        AuthUtils.handleUpdateUserSession(employeeDTO, session);
        return "redirect:/admin/profile";
    }

    @GetMapping("/admin/change-password")
    public String getChangePasswordPage(Model model) {
        EmployeeDTO currentEmployeeDTO = AuthUtils.getLoggedInEmployee(employeeService);
        model.addAttribute("passwordForm", new PasswordChangeDTO());
        return "admin/profile/change-password";
    }

    @PostMapping("/admin/change-password")
    public String changePassword(@ModelAttribute("passwordForm") @Valid PasswordChangeDTO passwordForm,
            BindingResult result,
            HttpSession session,
            Model model) {
        EmployeeDTO currentEmployeeDTO = AuthUtils.getLoggedInEmployee(employeeService);
        if (result.hasErrors()) {
            return "admin/profile/change-password";
        }
        employeeService.changePassword(currentEmployeeDTO.getEmployeeID(), passwordForm.getNewPassword());
        model.addAttribute("message", "Đổi mật khẩu thành công! Bạn sẽ được chuyển hướng sau 3 giây.");
        model.addAttribute("redirect", "/admin/profile");

        return "admin/profile/change-password";
    }

}
