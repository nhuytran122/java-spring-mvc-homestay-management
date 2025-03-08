package com.lullabyhomestay.homestay_management.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
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
    private Long roomTypeID;

    @NotBlank(message = "Vui lòng nhập tên loại phòng")
    @Column(name = "Name")
    private String name;

    @Min(value = 1, message = "Số lượng khách tối đa phải lớn hơn 0")
    @NotNull(message = "Vui lòng nhập số lượng khách tối đa")
    @Column(name = "MaxGuest")
    private Integer maxGuest;

    @Min(value = 1, message = "Giá phòng phải lớn hơn 0")
    @NotNull(message = "Vui lòng nhập giá loại phòng")
    @Column(name = "PricePerHour")
    private Double pricePerHour;

    @Column(name = "Description")
    private String description;

    @Min(value = 1, message = "Giá phòng bù giờ phải lớn hơn 0")
    @Column(name = "ExtraPricePerHour")
    private double extraPricePerHour;

    @OneToMany(mappedBy = "roomType")
    private List<Room> rooms;
}
