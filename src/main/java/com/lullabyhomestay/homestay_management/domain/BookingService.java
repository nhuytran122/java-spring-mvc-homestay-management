package com.lullabyhomestay.homestay_management.domain;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private Integer quantity;

    @Column(name = "TotalPrice")
    private Double totalPrice;

    @NotBlank(message = "Vui lòng ghi chú yêu cầu về dịch vụ (ngày, giờ, địa điểm đưa đón,...)")
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
