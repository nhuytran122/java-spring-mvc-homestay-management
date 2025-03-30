package com.lullabyhomestay.homestay_management.domain;

import java.time.LocalDateTime;

import com.lullabyhomestay.homestay_management.utils.PaymentStatus;
import com.lullabyhomestay.homestay_management.utils.RefundType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Refunds")
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RefundID")
    private Long refundID;

    @OneToOne
    @JoinColumn(name = "paymentID")
    private Payment payment;

    @Column(name = "CreatedAt", insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "RefundType")
    @Enumerated(EnumType.STRING)
    private RefundType refundType;

    @Column(name = "RefundAmount")
    private Double refundAmount;

    @Column(name = "ExternalTransactionID")
    private String externalTransactionID;
}
