package com.lullabyhomestay.homestay_management.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "InventoryStocks")
public class InventoryStock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StockID")
    private long stockID;

    @Column(name = "Quantity")
    private int quantity;
    
    @Column(name = "MinQuantity")
    private int minQuantity;

    @ManyToOne
    @JoinColumn(name = "ItemID")
    private InventoryItem inventoryItem;

    @ManyToOne
    @JoinColumn(name = "BranchID")
    private Branch branch;
}
