package com.lullabyhomestay.homestay_management.service.validator;

import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.dto.RegisterDTO;
import com.lullabyhomestay.homestay_management.service.CustomerService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RegisterValidator implements ConstraintValidator<RegisterChecked, RegisterDTO> {

    private final CustomerService customerService;

    @Override
    public boolean isValid(RegisterDTO user, ConstraintValidatorContext context) {
        boolean valid = true;

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            context.buildConstraintViolationWithTemplate("Mật khẩu không trùng khớp")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }
        if (this.customerService.checkEmailExistWhenRegister(user.getEmail())) {
            context.buildConstraintViolationWithTemplate("Email đã tồn tại")
                    .addPropertyNode("email")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }
        return valid;
    }
}
