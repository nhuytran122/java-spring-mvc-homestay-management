package com.lullabyhomestay.homestay_management.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking_pricing_snapshots")
public class BookingPricingSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long snapshotId;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private Integer baseDuration;

    private Double basePrice;

    private Double extraHourPrice;

    private Double overnightPrice;

    private Double dailyPrice;
}
