package com.lullabyhomestay.homestay_management.domain;

import java.util.Date;

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
@Table(name = "InventoryTransactions")
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransactionID")
    private long transactionID;

    @Column(name = "Quantity")
    private int quantity;

    @Column(name = "Date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "ItemID")
    private InventoryItem inventoryItem;

    @ManyToOne
    @JoinColumn(name = "BranchID")
    private Branch branch;
}