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
@Table(name = "BookingExtensions")
public class BookingExtension {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ExtensionID")
    private Long extensionID;

    @Column(name = "ExtendedHours")
    @NotNull(message = "Vui lòng nhập giờ muốn gia hạn")
    private Float extendedHours;

    @Column(name = "CreatedAt", insertable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "BookingID")
    private Booking booking;

    @OneToOne(mappedBy = "bookingExtension")
    private PaymentDetail paymentDetail;

    public Double getTotalAmount() {
        double pricePerHour = booking.getPricingSnapshot().getExtraHourPrice();
        boolean isDorm = booking.getRoom().getRoomType().getName().toLowerCase().contains("dorm");
        int guestCount = booking.getGuestCount();
        double adjustedPrice = isDorm ? pricePerHour * guestCount : pricePerHour;

        return adjustedPrice * extendedHours;
    }

}
