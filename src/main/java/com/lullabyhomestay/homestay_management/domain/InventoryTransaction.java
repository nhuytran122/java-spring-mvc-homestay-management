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
@Table(name = "inventory_transactions")
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    @NotNull(message = "Vui lòng nhập số lượng")
    private Integer quantity;

    @Column(insertable = false)
    private LocalDateTime createdAt;

    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "TransactionType")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @NotNull(message = "Vui lòng chọn đồ dùng")
    private InventoryItem inventoryItem;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    @NotNull(message = "Vui lòng chọn chi nhánh")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}