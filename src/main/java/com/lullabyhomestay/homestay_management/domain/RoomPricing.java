package com.lullabyhomestay.homestay_management.domain;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "room_pricings")
public class RoomPricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomPricingId;

    @Min(value = 1, message = "Thời gian cơ bản phải lớn hơn 0")
    @NotNull(message = "Vui lòng nhập thời gian cơ bản")
    private Integer baseDuration;

    @NotNull(message = "Vui lòng nhập giá cơ bản")
    @Min(value = 0, message = "Giá cơ bản phải lớn hơn hoặc bằng 0")
    private Double basePrice;

    @NotNull(message = "Vui lòng nhập giá bù giờ")
    @Min(value = 0, message = "Giá bù giờ phải lớn hơn hoặc bằng 0")
    private Double extraHourPrice;

    @NotNull(message = "Vui lòng nhập giá qua đêm")
    @Min(value = 0, message = "Giá qua đêm phải lớn hơn hoặc bằng 0")
    private Double overnightPrice;

    @NotNull(message = "Vui lòng nhập giá theo ngày")
    @Min(value = 0, message = "Giá theo ngày phải lớn hơn hoặc bằng 0")
    private Double dailyPrice;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;

    private String policy;

    private Boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;
}