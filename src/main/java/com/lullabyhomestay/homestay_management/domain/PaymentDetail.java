package com.lullabyhomestay.homestay_management.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(name = "Amount")
    private double amount;

    @Column(name = "ReferenceID")
    private Long referenceId;

    @ManyToOne
    @JoinColumn(name = "PaymentID")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "PaymentTypeID")
    private PaymentType paymentType;
}