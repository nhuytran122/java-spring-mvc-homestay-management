package com.lullabyhomestay.homestay_management.utils;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.lullabyhomestay.homestay_management.domain.dto.CustomerDTO;
import com.lullabyhomestay.homestay_management.domain.dto.EmployeeDTO;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.service.CustomerService;
import com.lullabyhomestay.homestay_management.service.EmployeeService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthUtils {

    public static CustomerDTO getLoggedInCustomer(CustomerService customerService) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Vui lòng đăng nhập để tiếp tục");
        }

        String email = authentication.getName();
        if (email == null) {
            throw new AccessDeniedException("Phiên đăng nhập không hợp lệ");
        }

        boolean hasCustomerRole = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_CUSTOMER".equals(authority.getAuthority()));
        if (!hasCustomerRole) {
            throw new AccessDeniedException("Nhân viên cần tài khoản khách hàng để sử dụng chức năng này");
        }

        try {
            CustomerDTO customerDTO = customerService.getCustomerDTOByEmail(email);
            return customerDTO;
        } catch (NotFoundException e) {
            throw new AccessDeniedException("Không tìm thấy tài khoản khách hàng với email: " + email);
        }
    }

    public static EmployeeDTO getLoggedInEmployee(EmployeeService employeeService) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Vui lòng đăng nhập để tiếp tục");
        }

        String email = authentication.getName();
        if (email == null) {
            throw new AccessDeniedException("Phiên đăng nhập không hợp lệ");
        }

        boolean hasCustomerRole = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_CUSTOMER".equals(authority.getAuthority()));
        if (hasCustomerRole) {
            throw new AccessDeniedException("Vui lòng đăng nhập tài khoản nhân viên để sử dụng chức năng này");
        }

        try {
            EmployeeDTO employeeDTO = employeeService.getEmployeeDTOByEmail(email);
            return employeeDTO;
        } catch (NotFoundException e) {
            throw new AccessDeniedException("Không tìm thấy tài khoản nhân viên với email: " + email);
        }
    }

    public static void handleUpdateUserSession(Object user, HttpSession session) {
        if (user instanceof EmployeeDTO) {
            EmployeeDTO employee = (EmployeeDTO) user;
            session.setAttribute("fullName", employee.getFullName());
            session.setAttribute("avatar", employee.getAvatar());
        } else if (user instanceof CustomerDTO) {
            CustomerDTO customer = (CustomerDTO) user;
            session.setAttribute("fullName", customer.getFullName());
            session.setAttribute("avatar", customer.getAvatar());
        } else {
            throw new IllegalArgumentException("Đối tượng không hợp lệ");
        }
    }

}