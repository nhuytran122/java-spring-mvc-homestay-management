package com.lullabyhomestay.homestay_management.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BookingPricingSnapshots")
public class BookingPricingSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SnapshotID")
    private Long snapshotID;

    @OneToOne
    @JoinColumn(name = "BookingID")
    private Booking booking;

    @Column(name = "BaseDuration")
    private Integer baseDuration;

    @Column(name = "BasePrice")
    private Double basePrice;

    @Column(name = "ExtraHourPrice")
    private Double extraHourPrice;

    @Column(name = "OvernightPrice")
    private Double overnightPrice;

    @Column(name = "DailyPrice")
    private Double dailyPrice;
}
