package com.lullabyhomestay.homestay_management.domain;

import com.lullabyhomestay.homestay_management.domain.id.RoomAmenityID;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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

    @EmbeddedId
    private RoomAmenityID roomAmenityID;

    @Column(name = "Quantity")
    private Integer quantity;

    @ManyToOne
    @MapsId("amenityID")
    @JoinColumn(name = "AmenityID")
    private Amenity amenity;

    @ManyToOne
    @MapsId("roomID")
    @JoinColumn(name = "RoomID")
    private Room room;

}
