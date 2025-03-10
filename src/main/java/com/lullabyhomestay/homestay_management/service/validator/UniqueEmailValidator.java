package com.lullabyhomestay.homestay_management.service.validator;

import org.springframework.stereotype.Service;
import com.lullabyhomestay.homestay_management.service.CustomerService;
import com.lullabyhomestay.homestay_management.service.EmployeeService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapperImpl;

@RequiredArgsConstructor
@Service
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, Object> {

    private final EmployeeService employeeService;
    private final CustomerService customerService;

    private String entityType;
    private String emailField;
    private String idField;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        this.entityType = constraintAnnotation.entityType();
        this.emailField = constraintAnnotation.emailField();
        this.idField = constraintAnnotation.idField();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        BeanWrapperImpl wrapper = new BeanWrapperImpl(obj);
        String email = (String) wrapper.getPropertyValue(emailField);
        Long id = wrapper.getPropertyValue(idField) != null ? (Long) wrapper.getPropertyValue(idField) : null;

        boolean valid = true;

        if ("EMPLOYEE".equalsIgnoreCase(entityType)) {
            if (employeeService.checkEmailExistForOther(email, id)) {
                context.buildConstraintViolationWithTemplate("Email đã tồn tại")
                        .addPropertyNode(emailField)
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
                valid = false;
            }
        } else if ("CUSTOMER".equalsIgnoreCase(entityType)) {
            if (customerService.checkEmailExistForOther(email, id)) {
                context.buildConstraintViolationWithTemplate("Email đã tồn tại")
                        .addPropertyNode(emailField)
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
                valid = false;
            }
        } else {
            context.buildConstraintViolationWithTemplate("EntityType phải là EMPLOYEE hoặc CUSTOMER")
                    .addPropertyNode(emailField)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }

        return valid;
    }
}