package com.lullabyhomestay.homestay_management.domain;

import java.text.Normalizer;
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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleID")
    private Long roleID;

    @NotBlank(message = "Vui lòng nhập tên vai trò")
    @Column(name = "RoleName")
    private String roleName;

    @Column(name = "Description")
    private String description;

    @OneToMany(mappedBy = "role")
    private List<Employee> employees;

    public String convertToSystemRoleName() {
        if (this.roleName == null) {
            return null;
        }
        String normalizedRoleName = Normalizer.normalize(this.roleName, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("đ", "d")
                .replaceAll("Đ", "D")
                .replaceAll("\\s+", "_")
                .replaceAll("[^A-Za-z0-9_]", "");
        return normalizedRoleName.toUpperCase();
    }

}
