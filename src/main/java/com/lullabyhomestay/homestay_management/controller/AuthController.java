package com.lullabyhomestay.homestay_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.dto.RegisterDTO;
import com.lullabyhomestay.homestay_management.domain.dto.ResetPasswordDTO;
import com.lullabyhomestay.homestay_management.service.CustomerService;
import com.lullabyhomestay.homestay_management.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class AuthController {
    private final UserService userService;
    private final CustomerService customerService;

    @GetMapping("/login")
    public String getLoginPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session.getAttribute("id") != null) {
            return "redirect:/";
        }
        return "shared/auth/login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session.getAttribute("id") != null) {
            return "redirect:/";
        }
        model.addAttribute("registerUser", new RegisterDTO());
        return "shared/auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("registerUser") @Valid RegisterDTO registerDTO,
            BindingResult result, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session.getAttribute("id") != null) {
            return "redirect:/";
        }
        if (result.hasErrors()) {
            return "shared/auth/register";
        }
        try {
            customerService.handleRegisterAccount(registerDTO);
            model.addAttribute("message", "Đăng ký thành công! Vui lòng kiểm tra email để xác nhận.");
            return "shared/auth/register";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "shared/auth/register";
        }
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session.getAttribute("id") != null) {
            return "redirect:/";
        }
        try {
            userService.verifyEmail(token);
            model.addAttribute("message", "Email đã được xác nhận! Bạn có thể đăng nhập.");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "shared/auth/verify-email";
    }

    @GetMapping("/verify-email-pending")
    public String showVerifyEmailPending(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session.getAttribute("id") != null) {
            return "redirect:/";
        }
        model.addAttribute("error", "Tài khoản chưa được kích hoạt. Vui lòng kiểm tra email để xác nhận.");
        return "shared/auth/verify-email-pending";
    }

    @GetMapping("/access-deny")
    public String getDenyPage(Model model) {
        return "shared/auth/deny";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "shared/auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        try {
            userService.requestPasswordReset(email);
            model.addAttribute("message",
                    "Yêu cầu đặt lại mật khẩu đã được gửi. Vui lòng kiểm tra email của bạn để tiếp tục.");
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
        }
        return "shared/auth/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        ResetPasswordDTO dto = new ResetPasswordDTO();
        dto.setToken(token);
        model.addAttribute("resetPasswordDTO", dto);
        return "shared/auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(
            @ModelAttribute("resetPasswordDTO") @Valid ResetPasswordDTO dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "shared/auth/reset-password";
        }
        try {
            userService.resetPassword(dto.getToken(), dto.getPassword());
            model.addAttribute("message", "Mật khẩu đã được đặt lại thành công!");
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
        }
        return "shared/auth/reset-password";
    }

}
