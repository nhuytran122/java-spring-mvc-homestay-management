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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "BookingServices")
public class BookingServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookingServiceID")
    private Long bookingServiceID;

    @Column(name = "Quantity")
    private Float quantity;

    @Column(name = "TotalPrice")
    private Double totalPrice;

    @Column(name = "IsAdditional")
    private Boolean isAdditional;

    @Column(name = "Description")
    private String description;

    @Column(name = "CreatedAt", insertable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "ServiceID")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "BookingID")
    private Booking booking;
}
