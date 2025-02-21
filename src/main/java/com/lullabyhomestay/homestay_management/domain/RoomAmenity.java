package com.lullabyhomestay.homestay_management.domain;

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
@Table(name = "RoomAmenities")
public class RoomAmenity {
    
    @Column(name = "Quantity")
    private int quantity;

    @Id
    @ManyToOne
    @JoinColumn(name = "AmenityID")
    private Amenity amenity;

    @Id
    @ManyToOne
    @JoinColumn(name = "RoomID")
    private Room room;
}
