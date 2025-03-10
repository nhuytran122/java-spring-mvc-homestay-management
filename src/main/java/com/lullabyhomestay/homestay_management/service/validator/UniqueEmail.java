package com.lullabyhomestay.homestay_management.service.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ ElementType.TYPE }) 
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "Email đã tồn tại";
    String entityType(); 
    String emailField() default "email";
    String idField();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}