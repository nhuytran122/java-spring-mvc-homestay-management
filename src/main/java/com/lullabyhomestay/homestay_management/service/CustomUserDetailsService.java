package com.lullabyhomestay.homestay_management.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.Role;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.lullabyhomestay.homestay_management.domain.User user = userService.getUserByEmail(username);
        if (user != null) {
            Role role = user.getRole();
            if (role == null || role.getRoleName() == null) {
                throw new UsernameNotFoundException("Người dùng không có vai trò hợp lệ.");
            }
            String roleName = "ROLE_" + role.getRoleName().name();
            return new User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(roleName)));
        }
        throw new UsernameNotFoundException("Không tìm thấy người dùng với email: " + username);
    }
}
