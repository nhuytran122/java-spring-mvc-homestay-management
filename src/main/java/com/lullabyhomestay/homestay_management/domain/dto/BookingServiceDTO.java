package com.lullabyhomestay.homestay_management.domain.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingServiceDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long serviceId;
    private String description;
    private Float quantity;
}
