package com.lullabyhomestay.homestay_management.service.validator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.User;
import com.lullabyhomestay.homestay_management.domain.dto.PasswordChangeDTO;
import com.lullabyhomestay.homestay_management.service.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PasswordChangeValidator implements ConstraintValidator<PasswordChangeChecked, PasswordChangeDTO> {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public boolean isValid(PasswordChangeDTO passwordForm, ConstraintValidatorContext context) {
        boolean valid = true;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            context.buildConstraintViolationWithTemplate("Vui lòng đăng nhập để tiếp tục")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            return false;
        }

        String email = authentication.getName();

        User user = userService.getUserByEmail(email);
        String currentPassword = user.getPassword();

        if (!passwordEncoder.matches(passwordForm.getOldPassword(), currentPassword)) {
            context.buildConstraintViolationWithTemplate("Mật khẩu cũ không đúng")
                    .addPropertyNode("oldPassword")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }

        if (!passwordForm.getNewPassword().equals(passwordForm.getConfirmPassword())) {
            context.buildConstraintViolationWithTemplate("Mật khẩu mới và xác nhận không khớp")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }
        return valid;
    }
}
