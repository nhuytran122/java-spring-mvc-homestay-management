package com.lullabyhomestay.homestay_management.domain;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private long paymentID;

    @Column(name = "PaymentMethod")
    private String paymentMethod;
    
    @Column(name = "Status")
    private boolean status;
    
    @Column(name = "PaymentDate")
    private Date paymentDate;
    
    @Column(name = "TotalAmount")
    private double totalAmount;

    @ManyToOne
    @JoinColumn(name = "BookingID")
    private Booking booking;

    @OneToMany(mappedBy = "payment")
    List<PaymentDetail> paymentDetails;
}
