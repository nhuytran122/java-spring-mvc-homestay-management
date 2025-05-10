package com.lullabyhomestay.homestay_management.domain.dto;

import com.lullabyhomestay.homestay_management.utils.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentConfirmationRequestDTO {
    private Long bookingID;
    private Long extensionID;
    private PaymentType paymentMethod;
}
