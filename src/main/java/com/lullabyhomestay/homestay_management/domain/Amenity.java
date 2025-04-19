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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Amenities")
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AmenityID")
    private Long amenityID;

    @NotBlank(message = "Vui lòng nhập tên tiện nghi")
    @Size(max = 255, message = "Tên tiện nghi không được vượt quá 255 ký tự")
    @Column(name = "AmenityName")
    private String amenityName;

    @ManyToOne
    @JoinColumn(name = "CategoryID")
    @NotNull(message = "Vui lòng chọn danh mục tiện nghi")
    private AmenityCategory amenityCategory;

    @OneToMany(mappedBy = "amenity")
    List<RoomAmenity> roomAmenities;
}
