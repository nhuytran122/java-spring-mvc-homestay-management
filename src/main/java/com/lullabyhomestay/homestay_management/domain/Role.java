package com.lullabyhomestay.homestay_management.domain;

import java.io.Serializable;
import java.util.List;

import com.lullabyhomestay.homestay_management.utils.SystemRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "Roles")
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleID")
    private Long roleID;

    @Column(name = "RoleName")
    @Enumerated(EnumType.STRING)
    private SystemRole roleName;

    @Column(name = "Description")
    private String description;

    @OneToMany(mappedBy = "role")
    private List<User> users;
}
