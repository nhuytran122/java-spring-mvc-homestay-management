package com.lullabyhomestay.homestay_management.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "RoomTypes")
public class RoomType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoomTypeID")
    private long roomTypeID;
    
    @NotBlank(message = "Vui lòng nhập tên loại phòng")
    @Column(name = "Name")
    private String name;
    
    @NotNull(message = "Vui lòng nhập số lượng khách tối đa")
    @Column(name = "MaxGuest")
    private int maxGuest;
    
    @Column(name = "Area")
    private float area;
    
    @NotNull(message = "Vui lòng nhập giá phòng mỗi đêm")
    @Column(name = "PricePerNight")
    private double pricePerNight;
    
    @Column(name = "Description")
    private String description;   
    
    @OneToMany(mappedBy = "roomType")
    private List<Room> rooms;
}
