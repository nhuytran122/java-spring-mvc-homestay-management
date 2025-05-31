package com.lullabyhomestay.homestay_management.domain;

import com.lullabyhomestay.homestay_management.utils.PaymentPurpose;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payment_details")
public class PaymentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentDetailId;

    @Enumerated(EnumType.STRING)
    private PaymentPurpose paymentPurpose;

    private Double baseAmount;

    private Double finalAmount;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToOne
    @JoinColumn(name = "extension_id")
    private BookingExtension bookingExtension;

    @OneToOne
    @JoinColumn(name = "booking_service_id")
    private BookingServices bookingService;
}