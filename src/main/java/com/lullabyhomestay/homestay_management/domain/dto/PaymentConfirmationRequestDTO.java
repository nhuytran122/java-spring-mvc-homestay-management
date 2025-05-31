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
    private Long bookingId;
    private Long extensionId;
    private PaymentType paymentMethod;
}
