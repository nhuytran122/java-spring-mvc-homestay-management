package com.lullabyhomestay.homestay_management.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.lullabyhomestay.homestay_management.utils.TransactionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private int quantity;

    @Column(name = "Date", insertable = false)
    private LocalDateTime date;

    @Column(name = "TransactionType")
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "ItemID")
    @NotNull(message = "Vui lòng chọn đồ dùng")
    private InventoryItem inventoryItem;

    @ManyToOne
    @JoinColumn(name = "BranchID")
    @NotNull(message = "Vui lòng chọn chi nhánh")
    private Branch branch;

    public String getFormattedDate() {
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return date.format(formatter);
        }
        return "N/A";
    }
}