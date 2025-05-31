package com.lullabyhomestay.homestay_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.Role;
import com.lullabyhomestay.homestay_management.utils.SystemRole;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAll();

    Optional<Role> findByRoleId(long roleId);

    Role findByRoleName(SystemRole role);
}
