package com.lullabyhomestay.homestay_management.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
public class BookingServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookingServiceID")
    private Long bookingServiceID;

    @NotNull(message = "Vui lòng nhập số lượng")
    @Column(name = "Quantity")
    private Float quantity;

    @Column(name = "Description")
    private String description;

    @Column(name = "CreatedAt", insertable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "ServiceID")
    @NotNull(message = "Vui lòng chọn dịch vụ")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "BookingID")
    @NotNull(message = "Vui lòng chọn booking")
    private Booking booking;

    @OneToOne(mappedBy = "bookingService")
    private PaymentDetail paymentDetail;

    public Double getRawTotalAmount() {
        return service.getPrice() * quantity;
    }
}
