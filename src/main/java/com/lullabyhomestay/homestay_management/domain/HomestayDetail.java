package com.lullabyhomestay.homestay_management.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "HomestayDetails")
public class HomestayDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InforID")
    private Long inforID;

    @NotBlank(message = "Vui lòng nhập tiêu đề thông tin")
    @Column(name = "Title")
    private String title;

    @NotBlank(message = "Vui lòng nhập mô tả")
    @Column(name = "Description")
    private String description;
}