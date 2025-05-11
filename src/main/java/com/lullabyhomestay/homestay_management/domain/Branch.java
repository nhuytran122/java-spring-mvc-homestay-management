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
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    private Long branchID;

    @NotBlank(message = "Vui lòng nhập tên chi nhánh")
    @Size(max = 255, message = "Tên chi nhánh không được vượt quá 255 ký tự")
    @Column(name = "BranchName")
    private String branchName;

    @NotBlank(message = "Vui lòng nhập địa chỉ")
    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    @Column(name = "Address")
    private String address;

    @Column(name = "Phone")
    @Pattern(message = "Số điện thoại không hợp lệ", regexp = "(?:\\+84|0084|0)[235789][0-9]{1,2}[0-9]{7}(?:[^\\d]+|$)")
    private String phone;

    @Column(name = "Image")
    private String image;

    @NotBlank(message = "Vui lòng nhập mật khẩu chi nhánh")
    @Column(name = "BranchPassword")
    private String branchPassword;

    @OneToMany(mappedBy = "branch")
    List<InventoryStock> inventoryStocks;

    @OneToMany(mappedBy = "branch")
    List<InventoryTransaction> inventoryTransactions;

    @OneToMany(mappedBy = "branch")
    List<Room> rooms;

    @OneToMany(mappedBy = "branch")
    List<MaintenanceRequest> maintenanceRequests;
}
