package com.lullabyhomestay.homestay_management.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.Employee;
import com.lullabyhomestay.homestay_management.domain.Role;
import com.lullabyhomestay.homestay_management.domain.dto.EmployeeDTO;
import com.lullabyhomestay.homestay_management.repository.EmployeeRepository;
import com.lullabyhomestay.homestay_management.repository.MaintenanceRequestRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final MaintenanceRequestRepository maintenanceRequestRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    public EmployeeDTO handleSaveEmployee(EmployeeDTO requestDTO) {
        // Employee employee = employeeDTOToEmployee(requestDTO);
        Employee employee = mapper.map(requestDTO, Employee.class);
        employee.setPassword(this.passwordEncoder.encode("lullabyhomestay"));
        Employee savedEmployee = employeeRepository.save(employee);
        return mapper.map(savedEmployee, EmployeeDTO.class);
    }

    public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        return employeePage.map(employee -> mapper.map(employee, EmployeeDTO.class));
    }

    public Page<EmployeeDTO> searchEmployees(String keyword, Boolean isWorking, Long roleId, int page) {
        Pageable pageable = PageRequest.of(page - 1, 2);
        Page<Employee> employeePage = employeeRepository.searchEmployees(keyword, isWorking, roleId, pageable);
        return employeePage.map(employee -> mapper.map(employee, EmployeeDTO.class));
    }

    public Optional<EmployeeDTO> getEmployeeByID(long employeeID) {
        Optional<Employee> employee = employeeRepository.findByEmployeeID(employeeID);
        if (employee.isPresent()) {
            return Optional.of(mapper.map(employee.get(), EmployeeDTO.class));
        }
        return Optional.empty();
    }

    public boolean canDeleteEmployee(long employeeID) {
        boolean hasMaintenanceRequest = maintenanceRequestRepository.existsByEmployee_EmployeeID(employeeID);
        return !(hasMaintenanceRequest);
    }

    @Transactional
    public void deleteByEmployeeID(long employeeID) {
        if (canDeleteEmployee(employeeID)) {
            employeeRepository.deleteByEmployeeID(employeeID);
        }
    }
}
