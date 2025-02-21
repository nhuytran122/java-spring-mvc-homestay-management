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
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookingID")
    private long bookingID;

    @NotBlank(message = "Vui lòng nhập ")
    @Column(name = "CheckIn")
    private Date checkIn;

    @NotBlank(message = "Vui lòng nhập ")
    @Column(name = "CheckOut")
    private Date checkOut;

    @Column(name = "Status")
    private int status;

    @Column(name = "CreatedAt")
    private Date createdAt;
    
    @Column(name = "TotalPrice")
    private double totalPrice;

    @OneToMany(mappedBy = "booking")
    List<BookingExtension> bookingExtensions;

    @OneToMany(mappedBy = "booking")
    List<BookingService> bookingServices;

    @OneToMany(mappedBy = "booking")
    List<Review> reviews;

    @OneToMany(mappedBy = "booking")
    List<Payment> payments;

    @ManyToOne
    @JoinColumn(name = "CustomerID")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "RoomID")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "BookingStatusID")
    private BookingStatus bookingStatus;
}
