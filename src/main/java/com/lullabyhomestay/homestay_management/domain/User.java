package com.lullabyhomestay.homestay_management.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Long userID;

    @Column(name = "Email")
    private String email;

    @Column(name = "FullName")
    private String fullName;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Address")
    private String address;

    @Column(name = "Avatar")
    private String avatar;

    @Column(name = "Password")
    private String password;

    @Column(name = "CreatedAt", insertable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "RoleID")
    private Role role;

    @OneToOne(mappedBy = "user")
    private Employee employee;

    @OneToOne(mappedBy = "user")
    private Customer customer;
}
