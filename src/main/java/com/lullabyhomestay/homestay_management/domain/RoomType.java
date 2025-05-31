package com.lullabyhomestay.homestay_management.domain;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
@Table(name = "room_types")
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;

    @NotBlank(message = "Vui lòng nhập tên loại phòng")
    @Size(max = 255, message = "Tên loại phòng không được vượt quá 255 ký tự")
    private String name;

    @Min(value = 1, message = "Số lượng khách tối đa phải lớn hơn 0")
    @NotNull(message = "Vui lòng nhập số lượng khách tối đa")
    private Integer maxGuest;

    private String photo;
    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String description;

    @OneToMany(mappedBy = "roomType")
    private List<Room> rooms;

    @Valid
    @OneToMany(mappedBy = "roomType")
    private List<RoomPricing> roomPricings;
}
