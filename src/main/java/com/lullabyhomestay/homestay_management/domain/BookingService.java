package com.lullabyhomestay.homestay_management.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class BookingService {

    @Id
    @Column(name = "BookingServiceID")
    private Long bookingServiceID;

    @Column(name = "Quantity")
    private int quantity;

    @Column(name = "CreatedAt")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "ServiceID")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "BookingID")
    private Booking booking;
}
