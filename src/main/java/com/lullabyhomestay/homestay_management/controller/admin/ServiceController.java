package com.lullabyhomestay.homestay_management.controller.admin;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.Service;
import com.lullabyhomestay.homestay_management.service.HomestayServiceService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class ServiceController {
    private final HomestayServiceService service;

    @GetMapping("/admin/service")
    public String getServicePage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String sort) {
        int validPage = Math.max(1, page);
        Page<Service> services = service.searchServices(keyword,
                validPage, sort);
        List<Service> listServices = services.getContent();

        StringBuilder extraParams = new StringBuilder();
        if (sort != null) {
            extraParams.append("&sort=").append(sort);
        }
        if (keyword != null && !keyword.isEmpty()) {
            extraParams.append("&keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8));
        }

        model.addAttribute("listServices", listServices);
        model.addAttribute("extraParams", extraParams);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("currentPage", validPage);
        model.addAttribute("totalPages", services.getTotalPages());
        return "admin/service/show";
    }

    @GetMapping("/admin/service/create")
    public String getCreateServicePage(Model model) {
        model.addAttribute("newService", new Service());
        return "admin/service/create";
    }

    @PostMapping("/admin/service/create")
    public String postCreateService(Model model,
            @ModelAttribute("newService") @Valid Service service,
            BindingResult newServiceBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

        if (newServiceBindingResult.hasErrors()) {
            return "admin/service/create";
        }
        this.service.handleSaveService(service);
        return "redirect:/admin/service";
    }

    @GetMapping("/admin/service/update/{id}")
    public String getUpdateServicePage(Model model, @PathVariable long id) {
        Service currentService = service.getServiceByID(id);
        model.addAttribute("service", currentService);
        return "admin/service/update";
    }

    @PostMapping("/admin/service/update")
    public String postUpdateService(Model model,
            @ModelAttribute("service") @Valid Service service,
            BindingResult newServiceBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        Service currentService = this.service.getServiceByID(service.getServiceID());
        if (newServiceBindingResult.hasErrors()) {
            return "admin/service/update";
        }
        currentService.setServiceName(service.getServiceName());
        currentService.setUnit(service.getUnit());
        currentService.setPrice(service.getPrice());
        currentService.setDescription(service.getDescription());

        this.service.handleSaveService(currentService);
        return "redirect:/admin/service";
    }

    @GetMapping("/admin/service/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteService(@PathVariable long id) {
        boolean canDelete = service.canDeleteService(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/admin/service/delete")
    public String postDeleteService(@RequestParam("serviceID") long serviceID) {
        this.service.deleteByServiceID(serviceID);
        return "redirect:/admin/service";
    }
}
