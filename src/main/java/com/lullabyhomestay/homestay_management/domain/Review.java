package com.lullabyhomestay.homestay_management.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Min(value = 1, message = "Vui lòng chọn số sao (từ 1 đến 5)")
    @Max(value = 5, message = "Số sao không được lớn hơn 5")
    private Integer rating;

    @NotBlank(message = "Vui lòng nhập nội dung đánh giá")
    private String comment;

    @Column(insertable = false)
    private LocalDateTime createdAt;

    private String image;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
