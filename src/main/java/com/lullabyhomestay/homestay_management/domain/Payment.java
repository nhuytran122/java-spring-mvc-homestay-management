package com.lullabyhomestay.homestay_management.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.lullabyhomestay.homestay_management.utils.PaymentStatus;
import com.lullabyhomestay.homestay_management.utils.PaymentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "Payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PaymentID")
    private Long paymentID;

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "ExternalTransactionID")
    private String externalTransactionID;

    @Column(name = "PaymentDate", insertable = false)
    private LocalDateTime paymentDate;

    @Column(name = "TotalAmount")
    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "BookingID")
    private Booking booking;

    @Column(name = "PaymentType")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @OneToMany(mappedBy = "payment")
    List<PaymentDetail> paymentDetails;
}
