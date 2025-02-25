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
import jakarta.validation.constraints.Min;
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
    @Min(value = 1, message = "Vui lòng nhập số phòng")
    @Column(name = "RoomNumber")
    private int roomNumber;

    @Column(name = "Area")
    private float area;

    @Column(name = "Description")
    private String description;

    @Column(name = "Thumbnail")
    private String thumbnail;

    @OneToMany(mappedBy = "room")
    private List<MaintenanceRequest> maintenanceRequests;

    @OneToMany(mappedBy = "room")
    private List<RoomPhoto> roomPhotos;

    @OneToMany(mappedBy = "room")
    private List<RoomAmenity> roomAmenities;

    @ManyToOne
    @JoinColumn(name = "BranchID")
    @NotNull(message = "Vui lòng chọn chi nhánh")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "RoomTypeID")
    @NotNull(message = "Vui lòng chọn loại phòng")
    private RoomType roomType;

    @OneToMany(mappedBy = "room")
    private List<RoomStatusHistory> roomStatusHistories;
}
