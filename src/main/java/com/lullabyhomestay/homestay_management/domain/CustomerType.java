package com.lullabyhomestay.homestay_management.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
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
@Table(name = "customer_types")
public class CustomerType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long customerTypeId;

    @NotBlank(message = "Vui lòng nhập tên phân loại khách hàng")
    @Size(max = 255, message = "Tên phân loại không được vượt quá 255 ký tự")
    private String name;

    @Size(max = 500, message = "Mô tả không được vượt quá 255 ký tự")
    private String description;

    @NotNull(message = "Vui lòng nhập phần trăm giảm giá")
    @Max(value = 100, message = "Phần trăm giảm giá không được vượt quá 100")
    @Min(value = 0, message = "Phần trăm giảm giá không được nhỏ hơn 0")
    private Float discountRate;

    @NotNull(message = "Vui lòng nhập số điểm tối thiểu")
    private Float minPoint;

    @OneToMany(mappedBy = "customerType")
    List<Customer> customers;
}
