package com.lullabyhomestay.homestay_management.config;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.lullabyhomestay.homestay_management.domain.Role;
import com.lullabyhomestay.homestay_management.domain.User;
import com.lullabyhomestay.homestay_management.service.UserService;
import com.lullabyhomestay.homestay_management.utils.SystemRole;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private UserService userService;

    protected String determineTargetUrl(final Authentication authentication) {
        Map<String, String> roleTargetUrlMap = new HashMap<>();
        roleTargetUrlMap.put("ROLE_" + SystemRole.CUSTOMER, "/");
        roleTargetUrlMap.put("ROLE_" + SystemRole.MANAGER, "/admin/dashboard");
        roleTargetUrlMap.put("ROLE_" + SystemRole.HOUSEKEEPER, "/admin/dashboard");
        roleTargetUrlMap.put("ROLE_" + SystemRole.EMPLOYEE, "/admin/dashboard");

        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (final GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if (roleTargetUrlMap.containsKey(authorityName)) {
                return roleTargetUrlMap.get(authorityName);
            }
        }
        return "/";
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, Authentication authentication)
            throws ServletException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new ServletException("Không tìm thấy người dùng với email: " + email);
        }
        if (!user.getIsEnabled()) {
            throw new ServletException("Tài khoản chưa được kích hoạt. Vui lòng kiểm tra email để xác nhận.");
        }

        Role role = user.getRole();
        session.setAttribute("fullName", user.getFullName());
        session.setAttribute("avatar", user.getAvatar());
        session.setAttribute("id", user.getUserID());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("role", role != null ? role.getRoleName().name() : SystemRole.CUSTOMER.name());
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            return;
        }

        clearAuthenticationAttributes(request, authentication);
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }
}