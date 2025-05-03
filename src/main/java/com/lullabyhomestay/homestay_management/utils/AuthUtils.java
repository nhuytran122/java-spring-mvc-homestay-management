package com.lullabyhomestay.homestay_management.utils;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.lullabyhomestay.homestay_management.domain.Customer;
import com.lullabyhomestay.homestay_management.domain.dto.CustomerDTO;
import com.lullabyhomestay.homestay_management.domain.dto.UserDTO;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthUtils {

    public static CustomerDTO getLoggedInCustomer(UserService userService, ModelMapper mapper) {
        UserDTO userDTO = getLoggedInUser(userService);

        if (userDTO.getCustomer() == null) {
            throw new AccessDeniedException("Vui lòng đăng nhập tài khoản khách hàng để sử dụng chức năng này");
        }
        Customer customer = userDTO.getCustomer();
        CustomerDTO customerDTO = mapper.map(customer, CustomerDTO.class);
        return customerDTO;
    }

    public static UserDTO getLoggedInUser(UserService userService) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Vui lòng đăng nhập để tiếp tục");
        }

        String email = authentication.getName();
        if (email == null) {
            throw new AccessDeniedException("Phiên đăng nhập không hợp lệ");
        }
        try {
            UserDTO userDTO = userService.getUserDTOByEmail(email);
            return userDTO;
        } catch (NotFoundException e) {
            throw new AccessDeniedException("Không tìm thấy tài khoản với email: " + email);
        }
    }

    public static Long getLoggedInUserID(HttpSession session) {
        Object idObj = session.getAttribute("id");
        if (idObj == null) {
            throw new AccessDeniedException("Chưa đăng nhập hoặc phiên hết hạn");
        }
        try {
            return Long.parseLong(idObj.toString());
        } catch (NumberFormatException e) {
            throw new AccessDeniedException("ID người dùng không hợp lệ trong session");
        }
    }

    public static void handleUpdateUserSession(UserDTO userDTO, HttpSession session) {
        session.setAttribute("fullName", userDTO.getFullName());
        session.setAttribute("avatar", userDTO.getAvatar());
    }

}