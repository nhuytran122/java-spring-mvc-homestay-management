package com.lullabyhomestay.homestay_management.service.validator;

import com.lullabyhomestay.homestay_management.domain.dto.ResetPasswordDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatchesChecked, ResetPasswordDTO> {

    @Override
    public boolean isValid(ResetPasswordDTO dto, ConstraintValidatorContext context) {
        if (dto.getPassword() == null || dto.getConfirmPassword() == null)
            return false;
        return dto.getPassword().equals(dto.getConfirmPassword());
    }
}
