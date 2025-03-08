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
@Table(name = "CustomerTypes")
public class CustomerType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "CustomerTypeID") 
    private Long customerTypeID;

    @NotBlank(message = "Vui lòng nhập tên phân loại khách hàng")
    @Column(name = "Name")  
    private String name;

    @Column(name = "Description")  
    private String description;

    @NotNull(message = "Vui lòng nhập phần trăm giảm giá")
    @Column(name = "DiscountRate")  
    private Float discountRate;

    @NotNull(message = "Vui lòng nhập số điểm tối thiểu")
    @Column(name = "MinPoint")
    private Float minPoint;
    
    @OneToMany(mappedBy = "customerType")
    List<Customer> customers;
}
