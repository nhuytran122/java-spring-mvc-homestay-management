package com.lullabyhomestay.homestay_management.domain.dto;

import com.lullabyhomestay.homestay_management.service.validator.PasswordMatchesChecked;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatchesChecked
public class ResetPasswordDTO {

    @NotBlank(message = "Yêu cầu không hợp lệ")
    private String token;

    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^A-Za-z0-9]).{8,}$", message = "Mật khẩu phải từ 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt")
    private String password;

    @NotBlank(message = "Vui lòng xác nhận mật khẩu")
    private String confirmPassword;
}
