package com.lullabyhomestay.homestay_management.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "RoomPhotos")
public class RoomPhoto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PhotoID")
    private long photoID;
    
    @Column(name = "DisplayOrder")
    private int displayOrder;
    
    @Column(name = "IsHidden")
    private boolean isHidden;

    @ManyToOne
    @JoinColumn(name = "RoomID")
    private Room room;
}
