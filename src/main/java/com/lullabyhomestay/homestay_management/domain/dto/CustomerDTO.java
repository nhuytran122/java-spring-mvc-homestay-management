package com.lullabyhomestay.homestay_management.domain.dto;

import com.lullabyhomestay.homestay_management.domain.CustomerType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerDTO extends UserDTO {

    private Long customerId;

    private Double rewardPoints;

    private CustomerType customerType;
}
