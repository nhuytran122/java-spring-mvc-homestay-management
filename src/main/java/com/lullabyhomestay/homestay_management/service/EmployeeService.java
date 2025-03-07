package com.lullabyhomestay.homestay_management.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.Employee;
import com.lullabyhomestay.homestay_management.domain.dto.EmployeeDTO;
import com.lullabyhomestay.homestay_management.domain.dto.SearchEmployeeCriterialDTO;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.EmployeeRepository;
import com.lullabyhomestay.homestay_management.repository.MaintenanceRequestRepository;
import com.lullabyhomestay.homestay_management.service.specifications.EmployeeSpecifications;
import com.lullabyhomestay.homestay_management.utils.Constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final MaintenanceRequestRepository maintenanceRequestRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    public EmployeeDTO handleSaveEmployee(EmployeeDTO requestDTO) {
        Employee employee = mapper.map(requestDTO, Employee.class);
        employee.setPassword(this.passwordEncoder.encode("lullabyhomestay"));
        Employee savedEmployee = employeeRepository.save(employee);
        return mapper.map(savedEmployee, EmployeeDTO.class);
    }

    public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        return employeePage.map(employee -> mapper.map(employee, EmployeeDTO.class));
    }

    public Page<EmployeeDTO> searchEmployees(SearchEmployeeCriterialDTO criteria, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE);
        Specification<Employee> spec = Specification.where(null);

        if (criteria.getKeyword() != null && !criteria.getKeyword().trim().isEmpty()) {
            spec = spec.and(Specification.where(EmployeeSpecifications.nameLike(criteria.getKeyword()))
                    .or(EmployeeSpecifications.addressLike(criteria.getKeyword()))
                    .or(EmployeeSpecifications.emailEqual(criteria.getKeyword()))
                    .or(EmployeeSpecifications.phoneEqual(criteria.getKeyword())));
        }
        if (criteria.getIsWorking() != null) {
            spec = spec.and(EmployeeSpecifications.isWorking(criteria.getIsWorking()));
        }
        if (criteria.getRoleID() != null) {
            spec = spec.and(EmployeeSpecifications.hasRole(criteria.getRoleID()));
        }
        return employeeRepository.findAll(spec, pageable).map(employee -> mapper.map(employee, EmployeeDTO.class));
    }

    public EmployeeDTO getEmployeeByID(long employeeID) {
        Optional<Employee> employeeOpt = employeeRepository.findByEmployeeID(employeeID);
        if (!employeeOpt.isPresent()) {
            throw new NotFoundException("Nhân viên");
        }
        return mapper.map(employeeOpt.get(), EmployeeDTO.class);
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
