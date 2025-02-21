package com.lullabyhomestay.homestay_management.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "InventoryItems")
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ItemID")
    private long itemID;

    @NotBlank(message = "Vui lòng nhập tên mặt hàng")
    @Column(name = "ItemName")
    private String itemName;

    @NotBlank(message = "Vui lòng nhập đơn vị")
    @Column(name = "Unit")
    private String unit;

    @NotBlank(message = "Vui lòng nhập giá")
    @Column(name = "Price")
    private double price;

    @OneToMany(mappedBy = "inventoryItem")
    private List<InventoryStock> inventoryStocks;

    @OneToMany(mappedBy = "inventoryItem")
    private List<InventoryTransaction> inventoryTransactions;

    @ManyToOne
    @JoinColumn(name = "CategoryID")
    private InventoryCategory inventoryCategory;
}
