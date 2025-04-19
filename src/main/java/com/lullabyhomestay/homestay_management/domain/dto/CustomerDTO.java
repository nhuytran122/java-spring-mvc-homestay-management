package com.lullabyhomestay.homestay_management.domain.dto;

import com.lullabyhomestay.homestay_management.domain.CustomerType;
import com.lullabyhomestay.homestay_management.service.validator.UniqueEmail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@UniqueEmail(entityType = "CUSTOMER", emailField = "email", idField = "customerID")
public class CustomerDTO {
    private Long customerID;

    @NotBlank(message = "Vui lòng nhập họ tên")
    @Size(max = 255, message = "Tên khách hàng không được vượt quá 255 ký tự")
    private String fullName;

    @NotBlank(message = "Vui lòng nhập số điện thoại")
    @Pattern(message = "Số điện thoại không hợp lệ", regexp = "(?:\\+84|0084|0)[235789][0-9]{1,2}[0-9]{7}(?:[^\\d]+|$)")
    private String phone;

    @Email(message = "Email không hợp lệ", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank(message = "Vui lòng nhập email")
    private String email;

    private String address;
    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    private String avatar;
    private Double rewardPoints;
    private Boolean enabled;
    private CustomerType customerType;
}
