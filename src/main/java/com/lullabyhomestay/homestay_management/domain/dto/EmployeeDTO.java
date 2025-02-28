package com.lullabyhomestay.homestay_management.domain.dto;

import com.lullabyhomestay.homestay_management.domain.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDTO {
    private Long employeeID;

    @NotBlank(message = "Vui lòng nhập họ tên")
    private String fullName;

    @NotBlank(message = "Vui lòng nhập số điện thoại")
    private String phone;

    @NotBlank(message = "Vui lòng nhập địa chỉ")
    private String address;

    private String email;
    private boolean isWorking;

    @NotNull(message = "Vui lòng nhập mức lương")
    private Double salary;

    private String avatar;

    @NotNull(message = "Vui lòng chọn vai trò")
    private Role role;
}
