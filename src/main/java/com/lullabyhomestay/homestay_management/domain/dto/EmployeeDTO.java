package com.lullabyhomestay.homestay_management.domain.dto;

import com.lullabyhomestay.homestay_management.domain.User;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeDTO extends UserDTO {
    @NotNull(message = "Vui lòng chọn vai trò")
    private Long employeeID;

    @NotNull(message = "Vui lòng nhập mức lương")
    @Min(value = 1, message = "Mức lương phải lớn hơn 0")
    private Double salary;

    private User user;
}
