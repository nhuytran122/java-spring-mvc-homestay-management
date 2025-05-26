package com.lullabyhomestay.homestay_management.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.lullabyhomestay.homestay_management.domain.dto.PasswordChangeDTO;
import com.lullabyhomestay.homestay_management.domain.dto.UserDTO;
import com.lullabyhomestay.homestay_management.service.EmployeeService;
import com.lullabyhomestay.homestay_management.service.UploadService;
import com.lullabyhomestay.homestay_management.service.UserService;
import com.lullabyhomestay.homestay_management.utils.AuthUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Controller
@RequestMapping("/admin/profile")
public class AdminUserProfileController {
    private final UploadService uploadService;
    private final UserService userService;
    private final EmployeeService employeeService;

    @GetMapping("")
    public String getProfilePage(Model model, HttpSession session) {
        Long userID = AuthUtils.getLoggedInUserID(session);
        model.addAttribute("user", employeeService.getEmployeeDTOByUserID(userID));
        return "admin/profile/show";
    }

    @GetMapping("/update")
    public String getUpdateProfilePage(Model model) {
        UserDTO userDTO = AuthUtils.getLoggedInUser(userService);
        model.addAttribute("user", userDTO);
        return "admin/profile/update";
    }

    @PostMapping("/update")
    public String postUpdateProfile(@ModelAttribute("user") @Valid UserDTO userDTO,
            BindingResult result,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long userID = AuthUtils.getLoggedInUserID(session);
        if (result.hasErrors())
            return "admin/profile/update";
        String img;
        if (!file.isEmpty()) {
            img = this.uploadService.handleSaveUploadFile(file, "avatar");
            userDTO.setAvatar(img);
        }
        userDTO = userService.updateProfile(userID, userDTO);
        AuthUtils.handleUpdateUserSession(userDTO, session);
        return "redirect:/admin/profile";
    }

    @GetMapping("/admin/change-password")
    public String getChangePasswordPage(Model model) {
        model.addAttribute("passwordForm", new PasswordChangeDTO());
        return "admin/profile/change-password";
    }

    @PostMapping("/admin/change-password")
    public String changePassword(@ModelAttribute("passwordForm") @Valid PasswordChangeDTO passwordForm,
            BindingResult result,
            HttpSession session,
            Model model) {
        Long userID = AuthUtils.getLoggedInUserID(session);
        if (result.hasErrors()) {
            return "admin/profile/change-password";
        }
        try {
            userService.changePassword(userID, passwordForm.getNewPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("message", "Đổi mật khẩu thành công! Bạn sẽ được chuyển hướng sau 3 giây.");
        model.addAttribute("redirect", "/admin/profile");

        return "admin/profile/change-password";
    }

}
