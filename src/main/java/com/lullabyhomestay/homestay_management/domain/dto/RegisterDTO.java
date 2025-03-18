package com.lullabyhomestay.homestay_management.domain.dto;

import com.lullabyhomestay.homestay_management.service.validator.RegisterChecked;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@RegisterChecked
public class RegisterDTO {
    @NotBlank(message = "Vui lòng nhập họ tên")
    private String fullName;

    @Email(message = "Email không hợp lệ", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    private String phone;

    @Size(min = 6, message = "Password phải có tối thiểu 6 ký tự")
    private String password;
    private String confirmPassword;
}
