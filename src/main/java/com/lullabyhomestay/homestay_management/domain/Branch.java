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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Branches")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "BranchID")
    private long branchID;

    @NotBlank(message = "Vui lòng nhập tên chi nhánh")
    @Column(name = "BranchName") 
    private String branchName;

    @NotBlank(message = "Vui lòng nhập địa chỉ")
    @Column(name = "Address") 
    private String address;

    @Column(name = "Phone") 
    private String phone;
    
    @Column(name = "Image") 
    private String image;

    @OneToMany(mappedBy = "branch")
    List<InventoryStock> inventoryStocks;

    @OneToMany(mappedBy = "branch")
    List<InventoryTransaction> inventoryTransactions;

    @OneToMany(mappedBy = "branch")
    List<Room> rooms;
}
