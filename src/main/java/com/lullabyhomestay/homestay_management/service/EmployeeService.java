package com.lullabyhomestay.homestay_management.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.Employee;
import com.lullabyhomestay.homestay_management.domain.User;
import com.lullabyhomestay.homestay_management.domain.dto.EmployeeDTO;
import com.lullabyhomestay.homestay_management.domain.dto.SearchEmployeeCriterialDTO;
import com.lullabyhomestay.homestay_management.domain.dto.UserDTO;
import com.lullabyhomestay.homestay_management.exception.CannotDeleteException;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.EmployeeRepository;
import com.lullabyhomestay.homestay_management.repository.InventoryTransactionRepository;
import com.lullabyhomestay.homestay_management.repository.MaintenanceRequestRepository;
import com.lullabyhomestay.homestay_management.service.specifications.EmployeeSpecifications;
import com.lullabyhomestay.homestay_management.utils.Constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final MaintenanceRequestRepository maintenanceRequestRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final ModelMapper mapper;
    private final UserService userService;

    @Transactional
    public EmployeeDTO handleCreateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = mapper.map(employeeDTO, Employee.class);

        // UserDTO userDTO = mapFromEmployeeDTO(employeeDTO);
        UserDTO userDTO = mapper.map(employeeDTO, UserDTO.class);

        Long roleID = employeeDTO.getRole().getRoleID();
        User user = userService.createUserForPerson(userDTO, roleID, null);
        employee.setUser(user);
        Employee savedEmployee = employeeRepository.save(employee);
        return mapper.map(savedEmployee, EmployeeDTO.class);
    }

    public EmployeeDTO mapToEmployeeDTO(Employee employee) {
        if (employee == null || employee.getUser() == null) {
            return null;
        }
        User user = employee.getUser();
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeID(employee.getEmployeeID());
        dto.setSalary(employee.getSalary());

        dto.setUserID(user.getUserID());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        dto.setAvatar(user.getAvatar());
        dto.setRole(user.getRole());
        return dto;
    }

    public void handleUpdateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = mapper.map(employeeDTO, Employee.class);
        User user = userService.getUserByUserID(employee.getUser().getUserID());

        user.setFullName(employeeDTO.getFullName());
        user.setAddress(employeeDTO.getAddress());
        user.setPhone(employeeDTO.getPhone());
        user.setAvatar(employeeDTO.getAvatar() != null ? employeeDTO.getAvatar() : user.getAvatar());
        user.setRole(employeeDTO.getRole());
        userService.handleSaveUser(user);
        this.handleSaveEmployee(employee);
    }

    public Employee handleSaveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // public UserDTO mapFromEmployeeDTO(EmployeeDTO dto) {
    // UserDTO userDTO = new UserDTO();
    // userDTO.setFullName(dto.getFullName());
    // userDTO.setPhone(dto.getPhone());
    // userDTO.setEmail(dto.getEmail());
    // userDTO.setAddress(dto.getAddress());
    // userDTO.setAvatar(dto.getAvatar());
    // return userDTO;
    // }

    public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        return employeePage.map(this::mapToEmployeeDTO);
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
        if (criteria.getRoleID() != null) {
            spec = spec.and(EmployeeSpecifications.hasRole(criteria.getRoleID()));
        }
        return employeeRepository.findAll(spec, pageable)
                .map(this::mapToEmployeeDTO);
    }

    public EmployeeDTO getEmployeeDTOByID(long employeeID) {
        Employee employee = employeeRepository.findByEmployeeID(employeeID)
                .orElseThrow(() -> new NotFoundException("Nhân viên"));

        return mapToEmployeeDTO(employee);
    }

    public EmployeeDTO getEmployeeDTOByUserID(long userID) {
        Employee employee = employeeRepository.findByUser_UserID(userID)
                .orElseThrow(() -> new NotFoundException("Nhân viên"));

        return mapToEmployeeDTO(employee);
    }

    public boolean canDeleteEmployee(long employeeID) {
        boolean hasMaintenanceRequest = maintenanceRequestRepository.existsByEmployee_EmployeeID(employeeID);
        boolean hasInventoryTransaction = inventoryTransactionRepository.existsByEmployee_EmployeeID(employeeID);
        return !(hasMaintenanceRequest || hasInventoryTransaction);
    }

    @Transactional
    public void deleteByEmployeeID(long employeeID) {
        if (!canDeleteEmployee(employeeID)) {
            throw new CannotDeleteException("Nhân viên");
        }
        employeeRepository.deleteByEmployeeID(employeeID);
    }
}
