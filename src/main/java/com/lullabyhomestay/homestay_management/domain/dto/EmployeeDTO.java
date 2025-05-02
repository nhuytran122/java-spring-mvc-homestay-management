package com.lullabyhomestay.homestay_management.domain.dto;

import com.lullabyhomestay.homestay_management.domain.Role;
import com.lullabyhomestay.homestay_management.service.validator.UniqueEmail;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@UniqueEmail(entityType = "EMPLOYEE", emailField = "email", idField = "employeeID")
public class EmployeeDTO extends PersonDTO {

    private Long employeeID;

    @NotNull(message = "Vui lòng nhập mức lương")
    private Double salary;

    @NotNull(message = "Vui lòng chọn vai trò")
    private Role role;
}
