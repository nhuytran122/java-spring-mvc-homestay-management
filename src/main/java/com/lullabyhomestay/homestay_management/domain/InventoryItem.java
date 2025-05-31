package com.lullabyhomestay.homestay_management.domain;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "inventory_items")
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @NotBlank(message = "Vui lòng nhập tên đồ dùng")
    @Size(max = 255, message = "Tên đồ dùng không được vượt quá 255 ký tự")
    private String itemName;

    @NotBlank(message = "Vui lòng nhập đơn vị tính")
    @Size(max = 100, message = "Đơn vị tính không được vượt quá 255 ký tự")
    private String unit;

    @Min(value = 1, message = "Giá cả phải lớn hơn 0")
    @NotNull(message = "Vui lòng nhập giá cả")
    private Double price;

    @OneToMany(mappedBy = "inventoryItem")
    private List<InventoryStock> inventoryStocks;

    @OneToMany(mappedBy = "inventoryItem")
    private List<InventoryTransaction> inventoryTransactions;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "Vui lòng chọn danh mục đồ dùng")
    private InventoryCategory inventoryCategory;
}
