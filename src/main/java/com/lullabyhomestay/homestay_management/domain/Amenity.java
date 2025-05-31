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
@Table(name = "amenities")
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long amenityId;

    @NotBlank(message = "Vui lòng nhập tên tiện nghi")
    @Size(max = 255, message = "Tên tiện nghi không được vượt quá 255 ký tự")
    private String amenityName;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "Vui lòng chọn danh mục tiện nghi")
    private AmenityCategory amenityCategory;

    @OneToMany(mappedBy = "amenity")
    List<RoomAmenity> roomAmenities;
}
