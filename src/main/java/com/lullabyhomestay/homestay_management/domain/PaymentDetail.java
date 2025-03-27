package com.lullabyhomestay.homestay_management.domain;

import com.lullabyhomestay.homestay_management.utils.PaymentPurpose;

import jakarta.persistence.Column;
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
@Table(name = "PaymentDetails")
public class PaymentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PaymentDetailID")
    private Long paymentDetailID;

    @Column(name = "PaymentPurpose")
    @Enumerated(EnumType.STRING)
    private PaymentPurpose paymentPurpose;

    @Column(name = "BaseAmount")
    private Double baseAmount;

    @Column(name = "FinalAmount")
    private Double finalAmount;

    @ManyToOne
    @JoinColumn(name = "PaymentID")
    private Payment payment;

    @OneToOne
    @JoinColumn(name = "ExtensionID")
    private BookingExtension bookingExtension;

    @OneToOne
    @JoinColumn(name = "BookingServiceID")
    private BookingServices bookingService;
}