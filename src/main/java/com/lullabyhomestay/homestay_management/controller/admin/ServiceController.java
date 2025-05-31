package com.lullabyhomestay.homestay_management.controller.admin;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.Service;
import com.lullabyhomestay.homestay_management.service.HomestayServiceService;
import com.lullabyhomestay.homestay_management.service.IconService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@PreAuthorize("hasAnyRole('MANAGER')")
@RequestMapping("/admin/service")
public class ServiceController {
    private final HomestayServiceService service;
    private final IconService iconService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
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

    @GetMapping("/create")
    public String getCreateServicePage(Model model) {
        model.addAttribute("newService", new Service());
        model.addAttribute("iconList", iconService.getCachedIconList());
        return "admin/service/create";
    }

    @PostMapping("/create")
    public String postCreateService(Model model,
            @ModelAttribute("newService") @Valid Service service,
            BindingResult newServiceBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

        if (newServiceBindingResult.hasErrors()) {
            model.addAttribute("iconList", iconService.getCachedIconList());
            return "admin/service/create";
        }
        this.service.handleSaveService(service);
        return "redirect:/admin/service";
    }

    @GetMapping("/update/{id}")
    public String getUpdateServicePage(Model model, @PathVariable long id) {
        Service currentService = service.getServiceById(id);
        model.addAttribute("iconList", iconService.getCachedIconList());
        model.addAttribute("service", currentService);
        return "admin/service/update";
    }

    @PostMapping("/update")
    public String postUpdateService(Model model,
            @ModelAttribute("service") @Valid Service service,
            BindingResult newServiceBindingResult,
            HttpServletRequest request) {

        Service currentService = this.service.getServiceById(service.getServiceId());
        if (newServiceBindingResult.hasErrors()) {
            model.addAttribute("iconList", iconService.getCachedIconList());
            return "admin/service/update";
        }
        currentService.setServiceName(service.getServiceName());
        currentService.setUnit(service.getUnit());
        currentService.setPrice(service.getPrice());
        currentService.setIsPrepaid(service.getIsPrepaid());
        currentService.setDescription(service.getDescription());
        currentService.setIcon(service.getIcon());

        this.service.handleSaveService(currentService);
        return "redirect:/admin/service";
    }

    @GetMapping("/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteService(@PathVariable long id) {
        boolean canDelete = service.canDeleteService(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/delete")
    public String postDeleteService(@RequestParam("serviceId") long serviceId) {
        this.service.deleteByServiceId(serviceId);
        return "redirect:/admin/service";
    }
}
