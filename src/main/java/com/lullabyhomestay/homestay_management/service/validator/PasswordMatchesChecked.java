package com.lullabyhomestay.homestay_management.service.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatchesChecked {
    String message() default "Mật khẩu và xác nhận mật khẩu không khớp";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
