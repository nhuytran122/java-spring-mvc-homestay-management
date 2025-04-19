package com.lullabyhomestay.homestay_management.domain.dto;

import com.lullabyhomestay.homestay_management.domain.Role;
import com.lullabyhomestay.homestay_management.service.validator.UniqueEmail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@UniqueEmail(entityType = "EMPLOYEE", emailField = "email", idField = "employeeID")
public class EmployeeDTO {
    private Long employeeID;

    @NotBlank(message = "Vui lòng nhập họ tên")
    @Size(max = 255, message = "Tên nhân viên không được vượt quá 255 ký tự")
    private String fullName;

    @NotBlank(message = "Vui lòng nhập số điện thoại")
    @Pattern(message = "Số điện thoại không hợp lệ", regexp = "(?:\\+84|0084|0)[235789][0-9]{1,2}[0-9]{7}(?:[^\\d]+|$)")
    private String phone;

    @NotBlank(message = "Vui lòng nhập địa chỉ")
    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    private String address;

    @Email(message = "Email không hợp lệ", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank(message = "Vui lòng nhập email")
    private String email;

    private Boolean isWorking;

    @NotNull(message = "Vui lòng nhập mức lương")
    private Double salary;

    private String avatar;

    @NotNull(message = "Vui lòng chọn vai trò")
    private Role role;
}
