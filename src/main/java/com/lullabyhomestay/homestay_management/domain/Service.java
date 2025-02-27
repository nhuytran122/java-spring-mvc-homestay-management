package com.lullabyhomestay.homestay_management.domain;

import java.util.List;

import jakarta.persistence.*;
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
@Table(name = "Services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ServiceID")
    private long serviceID;

    @NotBlank(message = "Vui lòng nhập tên dịch vụ")
    @Column(name = "ServiceName")
    private String serviceName;

    @NotBlank(message = "Vui lòng nhập đơn vị tính")
    @Column(name = "Unit")
    private String unit;

    @NotNull(message = "Vui lòng nhập giá dịch vụ")
    @Column(name = "Price")
    private double price;

    @Column(name = "Description")
    private String description;

    @OneToMany(mappedBy = "service")
    private List<BookingService> bookingServices;
}
