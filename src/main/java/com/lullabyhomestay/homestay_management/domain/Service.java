package com.lullabyhomestay.homestay_management.domain;

import java.util.List;

import jakarta.persistence.*;
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
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    @NotBlank(message = "Vui lòng nhập tên dịch vụ")
    @Size(max = 255, message = "Tên dịch vụ không được vượt quá 255 ký tự")
    private String serviceName;

    @NotBlank(message = "Vui lòng nhập đơn vị tính")
    @Size(max = 50, message = "Đơn vị tính không được vượt quá 50 ký tự")
    private String unit;

    @NotNull(message = "Vui lòng nhập giá dịch vụ")
    @Min(value = 0, message = "Gía dịch vụ phải lớn hơn hoặc bằng 0")
    private Double price;

    private Boolean isPrepaid;

    private String description;

    @NotBlank(message = "Vui lòng chọn icon")
    private String icon;

    @OneToMany(mappedBy = "service")
    private List<BookingServices> bookingServices;

}
