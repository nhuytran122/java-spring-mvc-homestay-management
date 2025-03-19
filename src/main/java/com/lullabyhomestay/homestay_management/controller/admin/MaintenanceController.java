package com.lullabyhomestay.homestay_management.controller.admin;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lullabyhomestay.homestay_management.domain.Employee;
import com.lullabyhomestay.homestay_management.domain.MaintenanceRequest;
import com.lullabyhomestay.homestay_management.domain.dto.EmployeeDTO;
import com.lullabyhomestay.homestay_management.domain.dto.RoomDTO;
import com.lullabyhomestay.homestay_management.domain.dto.SearchMaintenanceCriteriaDTO;
import com.lullabyhomestay.homestay_management.service.BranchService;
import com.lullabyhomestay.homestay_management.service.EmployeeService;
import com.lullabyhomestay.homestay_management.service.MaintenanceRequestService;
import com.lullabyhomestay.homestay_management.service.RoomService;
import com.lullabyhomestay.homestay_management.service.UploadService;
import com.lullabyhomestay.homestay_management.utils.MaintenanceStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class MaintenanceController {
    private final MaintenanceRequestService maintenanceService;
    private final BranchService branchService;
    private final UploadService uploadService;
    private final RoomService roomService;
    private final EmployeeService employeeService;
    private final ModelMapper mapper;

    @GetMapping("/admin/maintenance")
    public String getMaintenancePage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @ModelAttribute SearchMaintenanceCriteriaDTO criteria) {
        int validPage = Math.max(1, page);
        String sort = (criteria.getSort() != null && !criteria.getSort().isEmpty()) ? criteria.getSort() : "desc";
        criteria.setSort(sort);
        Page<MaintenanceRequest> maintenances = maintenanceService.searchMaintenances(criteria, validPage);
        List<MaintenanceRequest> listMaintenances = maintenances.getContent();

        model.addAttribute("criteria", criteria);
        model.addAttribute("extraParams", criteria.convertToExtraParams());
        model.addAttribute("listMaintenances", listMaintenances);
        model.addAttribute("currentPage", validPage);
        model.addAttribute("totalPages", maintenances.getTotalPages());
        model.addAttribute("maintenanceStatuses", MaintenanceStatus.values());
        model.addAttribute("listBranches", this.branchService.getAllBranches());
        return "admin/maintenance/show";
    }

    @GetMapping("/admin/maintenance/{id}")
    public String getDetailRequest(Model model, @PathVariable("id") Long id) {
        MaintenanceRequest request = maintenanceService.getMaintenanceRequestByID(id);
        model.addAttribute("request", request);
        return "admin/maintenance/detail";
    }

    @GetMapping("/admin/maintenance/create")
    public String getCreateMaintenancePage(Model model) {
        model.addAttribute("newRequest", new MaintenanceRequest());
        model.addAttribute("listBranches", this.branchService.getAllBranches());
        return "admin/maintenance/create";
    }

    @GetMapping("/admin/maintenance/rooms-by-branch")
    @ResponseBody
    public List<RoomDTO> getRoomsByBranch(@RequestParam("branchID") Long branchID) {
        List<RoomDTO> roomDTOs = roomService.getRoomDTOsByBranchID(branchID);
        return roomDTOs;
    }

    @PostMapping("/admin/maintenance/create")
    public String postCreateMaintenance(Model model,
            @ModelAttribute("newRequest") @Valid MaintenanceRequest maintenance,
            BindingResult result,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("listBranches", this.branchService.getAllBranches());
            return "admin/maintenance/create";
        }
        String img;
        if (!file.isEmpty()) {
            img = this.uploadService.handleSaveUploadFile(file, "maintenance");
            maintenance.setImage(img);
            ;
        }
        if (maintenance.getRoom() != null && maintenance.getRoom().getRoomID() == null) {
            maintenance.setRoom(null);
        }
        HttpSession session = request.getSession(false);
        Long employeeID = (Long) session.getAttribute("id");
        EmployeeDTO employee = employeeService.getEmployeeDTOByID(employeeID);
        maintenance.setEmployee(mapper.map(employee, Employee.class));
        maintenance.setStatus(MaintenanceStatus.PENDING);
        this.maintenanceService.handleSaveMaintenanceRequest(maintenance);
        return "redirect:/admin/maintenance";
    }

    @GetMapping("/admin/maintenance/can-update/{id}")
    public ResponseEntity<Boolean> canUpdateRequest(@PathVariable Long id) {
        boolean canUpdate = maintenanceService.canUpdateRequest(id);
        return ResponseEntity.ok(canUpdate);
    }

    @GetMapping("/admin/maintenance/update/{id}")
    public String getUpdateMaintenancePage(Model model, @PathVariable Long id) {
        MaintenanceRequest request = maintenanceService.getMaintenanceRequestByID(id);
        boolean canUpdate = maintenanceService.canUpdateRequest(id);
        model.addAttribute("request", request);
        if (!canUpdate) {
            model.addAttribute("canUpdate", false);
            return "admin/maintenance/update";
        }
        model.addAttribute("listBranches", this.branchService.getAllBranches());
        List<RoomDTO> listRooms = roomService.getRoomDTOsByBranchID(request.getBranch().getBranchID());
        model.addAttribute("listRooms", listRooms);
        return "admin/maintenance/update";
    }

    @PostMapping("/admin/maintenance/update")
    public String postUpdateBranch(Model model,
            @ModelAttribute("request") @Valid MaintenanceRequest maintenanceRequest,
            BindingResult result,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        MaintenanceRequest currentRequest = this.maintenanceService
                .getMaintenanceRequestByID(maintenanceRequest.getRequestID());
        if (result.hasErrors()) {
            return "admin/maintenance/update";
        }
        if (!file.isEmpty()) {
            String img = this.uploadService.handleSaveUploadFile(file, "maintenance");
            currentRequest.setImage(img);
        }
        if (maintenanceRequest.getRoom() != null && maintenanceRequest.getRoom().getRoomID() == null) {
            currentRequest.setRoom(null);
        }
        currentRequest.setBranch(maintenanceRequest.getBranch());
        currentRequest.setStatus(maintenanceRequest.getStatus());
        currentRequest.setDescription(maintenanceRequest.getDescription());
        currentRequest.setUpdatedAt(LocalDateTime.now());
        this.maintenanceService.handleSaveMaintenanceRequest(currentRequest);
        return "redirect:/admin/maintenance";
    }

    @PostMapping("/admin/maintenance/update-status")
    @ResponseBody
    public ResponseEntity<?> updateMaintenanceStatus(
            @RequestParam("requestID") Long requestID,
            @RequestParam("status") String status) {
        MaintenanceRequest maintenance = maintenanceService.getMaintenanceRequestByID(requestID);
        MaintenanceStatus currentStatus = maintenance.getStatus();
        MaintenanceStatus newStatus = MaintenanceStatus.valueOf(status.toUpperCase());

        if (currentStatus == MaintenanceStatus.COMPLETED) {
            return ResponseEntity.badRequest().body("Bảo trì đã hoàn thành, không thể cập nhật!");
        }
        if (currentStatus == MaintenanceStatus.CANCELLED) {
            return ResponseEntity.badRequest().body("Bảo trì đã bị hủy, không thể cập nhật!");
        }
        if (currentStatus == MaintenanceStatus.PENDING && newStatus == MaintenanceStatus.PENDING) {
            return ResponseEntity.badRequest().body("Trạng thái đang chờ xử lý, không cần cập nhật lại!");
        }
        if (currentStatus == MaintenanceStatus.IN_PROGRESS &&
                (newStatus == MaintenanceStatus.PENDING || newStatus == MaintenanceStatus.IN_PROGRESS)) {
            return ResponseEntity.badRequest().body("Đang xử lý, không thể quay lại chờ hoặc giữ nguyên!");
        }
        maintenance.setStatus(newStatus);
        maintenance.setUpdatedAt(LocalDateTime.now());
        maintenanceService.handleSaveMaintenanceRequest(maintenance);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/maintenance/delete")
    public String postDeleteRequest(@RequestParam("requestID") Long requestID) {
        this.maintenanceService.deleteByMaintenanceRequestID(requestID);
        return "redirect:/admin/maintenance";
    }
}
