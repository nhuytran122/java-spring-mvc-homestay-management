package com.lullabyhomestay.homestay_management.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;

@Controller
public class DashboardController {

    @GetMapping("/admin")
    public String getDashboard(Model model) {
        return "admin/dashboard/show";
    }
}
