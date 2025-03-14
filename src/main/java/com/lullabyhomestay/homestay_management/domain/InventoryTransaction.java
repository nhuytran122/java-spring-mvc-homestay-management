package com.lullabyhomestay.homestay_management.domain;

import java.time.LocalDateTime;

import com.lullabyhomestay.homestay_management.utils.TransactionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
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
@Table(name = "InventoryTransactions")
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransactionID")
    private Long transactionID;

    @Column(name = "Quantity")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    @NotNull(message = "Vui lòng số lượng")
    private Integer quantity;

    @Column(name = "CreatedAt", insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "TransactionType")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "ItemID")
    @NotNull(message = "Vui lòng chọn đồ dùng")
    private InventoryItem inventoryItem;

    @ManyToOne
    @JoinColumn(name = "BranchID")
    @NotNull(message = "Vui lòng chọn chi nhánh")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "EmployeeID")
    private Employee employee;
}