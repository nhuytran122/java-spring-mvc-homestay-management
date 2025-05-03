package com.lullabyhomestay.homestay_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.Role;
import com.lullabyhomestay.homestay_management.repository.RoleRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return this.roleRepository.findAll();
    }

}
