package com.lullabyhomestay.homestay_management.domain.dto;

import com.lullabyhomestay.homestay_management.domain.CustomerType;
import com.lullabyhomestay.homestay_management.service.validator.UniqueEmail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@UniqueEmail(entityType = "CUSTOMER", emailField = "email", idField = "customerID")
public class CustomerDTO extends PersonDTO {

    private Long customerID;

    private Double rewardPoints;

    private CustomerType customerType;
}
