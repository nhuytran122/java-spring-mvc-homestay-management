package com.lullabyhomestay.homestay_management.domain;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.lullabyhomestay.homestay_management.utils.BookingStatus;

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
    private Long bookingID;

    @NotBlank(message = "Vui lòng nhập giờ checkin")
    @Column(name = "CheckIn")
    private LocalDateTime checkIn;

    @NotBlank(message = "Vui lòng nhập giờ checkout")
    @Column(name = "CheckOut")
    private LocalDateTime checkOut;

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "CreatedAt")
    private Date createdAt;

    @Column(name = "TotalAmount")
    private Double totalAmount;

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

    @OneToMany(mappedBy = "booking")
    List<RoomStatusHistory> roomStatusHistories;
}
