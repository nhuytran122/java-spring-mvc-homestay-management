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

import com.lullabyhomestay.homestay_management.domain.Customer;
import com.lullabyhomestay.homestay_management.domain.Employee;
import com.lullabyhomestay.homestay_management.service.CustomerService;
import com.lullabyhomestay.homestay_management.service.EmployeeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    protected String determineTargetUrl(final Authentication authentication) {

        Map<String, String> roleTargetUrlMap = new HashMap<>();
        roleTargetUrlMap.put("ROLE_CUSTOMER", "/");
        roleTargetUrlMap.put("ROLE_QUAN_LY", "/admin");
        roleTargetUrlMap.put("ROLE_NHAN_VIEN", "/admin");
        roleTargetUrlMap.put("ROLE_NHAN_VIEN_DON_DEP", "/admin");

        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if (roleTargetUrlMap.containsKey(authorityName)) {
                return roleTargetUrlMap.get(authorityName);
            }
        }
        return "/";
    }

    @Autowired
    private CustomerService customerService;
    @Autowired
    private EmployeeService employeeService;

    protected void clearAuthenticationAttributes(HttpServletRequest request, Authentication authentication) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        String email = authentication.getName();
        Customer customer = this.customerService.getCustomerByEmail(email);
        if (customer != null) {
            session.setAttribute("fullName", customer.getFullName());
            session.setAttribute("avatar", customer.getAvatar());
            session.setAttribute("id", customer.getCustomerID());
            session.setAttribute("email", customer.getEmail());
            return;
        }

        Employee employee = this.employeeService.getEmployeeByEmail(email);
        if (employee != null) {
            session.setAttribute("fullName", employee.getFullName());
            session.setAttribute("avatar", employee.getAvatar());
            session.setAttribute("id", employee.getEmployeeID());
            session.setAttribute("email", employee.getEmail());
        }
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
        clearAuthenticationAttributes(request, authentication);
    }

}