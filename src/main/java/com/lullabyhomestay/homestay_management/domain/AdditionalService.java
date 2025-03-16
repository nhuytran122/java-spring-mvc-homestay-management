package com.lullabyhomestay.homestay_management.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AdditionalServices")
public class AdditionalService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AdditionalServiceID")
    private Long additionalServiceID;

    @Column(name = "Quantity")
    private Float quantity;

    @Column(name = "UsageDate", insertable = false)
    private LocalDateTime usageDate;

    @Column(name = "TotalPrice")
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "BookingID")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "ServiceID")
    private Service service;

}
