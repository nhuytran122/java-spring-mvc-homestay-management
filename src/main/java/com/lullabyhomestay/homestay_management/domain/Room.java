package com.lullabyhomestay.homestay_management.domain;

import java.util.List;

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
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @NotNull(message = "Vui lòng nhập số phòng")
    private Integer roomNumber;

    private Float area;
    private String thumbnail;

    private Boolean isActive = true;

    @OneToMany(mappedBy = "room")
    private List<MaintenanceRequest> maintenanceRequests;

    @OneToMany(mappedBy = "room")
    private List<RoomPhoto> roomPhotos;

    @OneToMany(mappedBy = "room")
    private List<RoomAmenity> roomAmenities;

    @OneToMany(mappedBy = "room")
    private List<Booking> bookings;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    @NotNull(message = "Vui lòng chọn chi nhánh")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    @NotNull(message = "Vui lòng chọn loại phòng")
    private RoomType roomType;

    @OneToMany(mappedBy = "room")
    private List<RoomStatusHistory> roomStatusHistories;
}
