package com.lullabyhomestay.homestay_management.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.Customer;
import com.lullabyhomestay.homestay_management.domain.Employee;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerService customerService;
    private final EmployeeService employeeService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerService.getCustomerByEmail(username);
        if (customer != null) {
            return new User(
                    customer.getEmail(),
                    customer.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
        }

        Employee employee = employeeService.getEmployeeByEmail(username);
        if (employee == null) {
            throw new UsernameNotFoundException("Không tìm thấy người dùng: " + username);
        }

        if (!employee.getIsWorking()) {
            throw new UsernameNotFoundException(
                    "Tài khoản của bạn hiện không đủ quyền truy cập trang này! Vui lòng liên hệ quản trị viên để được hỗ trợ.");
        }

        return new User(
                employee.getEmail(),
                employee.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + employee.getRole().convertToSystemRoleName())));
    }
}
