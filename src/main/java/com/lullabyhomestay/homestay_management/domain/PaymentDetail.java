package com.lullabyhomestay.homestay_management.domain;

import com.lullabyhomestay.homestay_management.utils.PaymentCategory;
import com.lullabyhomestay.homestay_management.utils.PaymentStatus;
import com.lullabyhomestay.homestay_management.utils.TransactionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    
    @Column(name = "PaymentCategory")
    @Enumerated(EnumType.STRING)
    private PaymentCategory paymentCategory;


    @Column(name = "ReferenceID")
    private Long referenceId;

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "Amount")
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "PaymentID")
    private Payment payment;
}