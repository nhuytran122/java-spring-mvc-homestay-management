package com.lullabyhomestay.homestay_management.domain;

import java.util.List;

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
@Table(name = "inventory_categories")
public class InventoryCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @NotBlank(message = "Vui lòng nhập tên danh mục")
    @Size(max = 255, message = "Tên danh mục không được vượt quá 255 ký tự")
    private String categoryName;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String description;

    @OneToMany(mappedBy = "inventoryCategory")
    private List<InventoryItem> inventoryItems;
}
