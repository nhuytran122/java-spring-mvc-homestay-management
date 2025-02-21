package com.lullabyhomestay.homestay_management.domain;

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
@Table(name = "Rooms")
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoomID")
    private long roomID;

    @NotNull(message = "Vui lòng nhập số phòng")
    @Column(name = "RoomNumber")
    private int roomNumber;

    @Column(name = "Status")
    private boolean status;

    @Column(name = "Area")
    private float area;

    @Column(name = "Description")
    private String description;

    @OneToMany(mappedBy = "room")
    private List<MaintainanceRequest> maintainanceRequests;

    @OneToMany(mappedBy = "room")
    private List<RoomPhoto> roomPhotos;

    @OneToMany(mappedBy = "room")
    private List<RoomAmenity> roomAmenities;

    @ManyToOne
    @JoinColumn(name = "BranchID")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "RoomTypeID")
    private RoomType roomType;
}
