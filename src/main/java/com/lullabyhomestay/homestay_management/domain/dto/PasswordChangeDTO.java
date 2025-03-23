package com.lullabyhomestay.homestay_management.domain.dto;

import com.lullabyhomestay.homestay_management.service.validator.PasswordChangeChecked;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@PasswordChangeChecked
public class PasswordChangeDTO {
    @NotBlank(message = "Vui lòng nhập mật khẩu cũ")
    private String oldPassword;

    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^A-Za-z0-9]).{8,}$", message = "Mật khẩu mới phải có tối thiểu 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt")
    private String newPassword;

    @NotBlank(message = "Vui lòng xác nhận mật khẩu mới")
    private String confirmPassword;
}
