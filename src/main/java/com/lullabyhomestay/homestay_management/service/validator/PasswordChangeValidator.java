package com.lullabyhomestay.homestay_management.service.validator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.Customer;
import com.lullabyhomestay.homestay_management.domain.Employee;
import com.lullabyhomestay.homestay_management.domain.dto.PasswordChangeDTO;
import com.lullabyhomestay.homestay_management.service.CustomerService;
import com.lullabyhomestay.homestay_management.service.EmployeeService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PasswordChangeValidator implements ConstraintValidator<PasswordChangeChecked, PasswordChangeDTO> {

    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;

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
        String role = "";

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            role = authority.getAuthority();
            break;
        }

        String currentPassword = null;

        if ("ROLE_CUSTOMER".equals(role)) {
            Customer customer = customerService.getCustomerByEmail(email);
            if (customer != null) {
                currentPassword = customer.getPassword();
            }
        } else {
            Employee employee = employeeService.getEmployeeByEmail(email);
            if (employee != null) {
                currentPassword = employee.getPassword();
            }
        }

        if (currentPassword == null) {
            context.buildConstraintViolationWithTemplate("Không tìm thấy tài khoản phù hợp")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            return false;
        }

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
