package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.Role;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.EmployeeRepository;
import com.lullabyhomestay.homestay_management.repository.RoleRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;

    public List<Role> getAllRoles() {
        return this.roleRepository.findAll();
    }

    public Role getRoleByRoleID(long roleID) {
        Optional<Role> roleOpt = this.roleRepository.findByRoleID(roleID);
        if (!roleOpt.isPresent()) {
            throw new NotFoundException("Vai trò");
        }
        return roleOpt.get();
    }

    public Role handleSaveRole(Role role) {
        return this.roleRepository.save(role);
    }

    public boolean canDeleteRole(long roleID) {
        return !employeeRepository.existsByRole_RoleID(roleID);
    }

    @Transactional
    public void deleteByRoleID(long roleID) {
        if (canDeleteRole(roleID)) {
            this.roleRepository.deleteByRoleID(roleID);
        }
    }
}
