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
    @Column(name = "DetailID")
    private long detailID;

    @NotBlank(message = "Vui lòng nhập tên thuộc tính")
    @Column(name = "AttributeName")
    private String attributeName;

    @NotBlank(message = "Vui lòng nhập giá trị thuộc tính")
    @Column(name = "AttributeValue")
    private String attributeValue;
}