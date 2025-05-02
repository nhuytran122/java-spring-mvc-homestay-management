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
@Table(name = "Employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmployeeID")
    private Long employeeID;

    @NotBlank(message = "Vui lòng nhập họ tên nhân viên")
    @Column(name = "FullName")
    private String fullName;

    @NotBlank(message = "Vui lòng nhập số điện thoại")
    @Column(name = "Phone")
    private String phone;

    @Column(name = "Email")
    private String email;

    @NotBlank(message = "Vui lòng nhập địa chỉ")
    @Column(name = "Address")
    private String address;

    @Column(name = "Avatar")
    private String avatar;

    @Column(name = "Password")
    private String password;

    @NotNull(message = "Vui lòng nhập mức lương")
    @Column(name = "Salary")
    private Double salary;

    @ManyToOne
    @NotNull(message = "Vui lòng chọn vai trò")
    @JoinColumn(name = "RoleID")
    private Role role;

    @OneToMany(mappedBy = "employee")
    private List<MaintenanceRequest> maintenanceRequests;

    @OneToMany(mappedBy = "employee")
    private List<InventoryTransaction> inventoryTransactions;

    @OneToMany(mappedBy = "employee")
    List<ActionLog> actionLogs;
}
