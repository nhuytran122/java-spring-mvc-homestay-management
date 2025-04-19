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
@Table(name = "AmenityCategories")
public class AmenityCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryID")
    private Long categoryID;

    @NotBlank(message = "Vui lòng nhập tên phân loại")
    @Size(max = 255, message = "Tên phân loại không được vượt quá 255 ký tự")
    @Column(name = "CategoryName")
    private String categoryName;

    @NotBlank(message = "Vui lòng chọn icon")
    @Column(name = "Icon")
    private String icon;

    @Column(name = "Description")
    @Size(max = 500, message = "Mô tả không được vượt quá 255 ký tự")
    private String description;

    @OneToMany(mappedBy = "amenityCategory")
    List<Amenity> amenities;

}
