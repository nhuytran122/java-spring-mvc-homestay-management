package com.lullabyhomestay.homestay_management.controller.admin;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lullabyhomestay.homestay_management.domain.MaintenanceRequest;
import com.lullabyhomestay.homestay_management.domain.User;
import com.lullabyhomestay.homestay_management.domain.dto.RoomDTO;
import com.lullabyhomestay.homestay_management.domain.dto.SearchMaintenanceCriteriaDTO;
import com.lullabyhomestay.homestay_management.service.BranchService;
import com.lullabyhomestay.homestay_management.service.MaintenanceRequestService;
import com.lullabyhomestay.homestay_management.service.RoomService;
import com.lullabyhomestay.homestay_management.service.UploadService;
import com.lullabyhomestay.homestay_management.service.UserService;
import com.lullabyhomestay.homestay_management.utils.MaintenanceStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/admin/maintenance")
public class MaintenanceController {
    private final MaintenanceRequestService maintenanceService;
    private final BranchService branchService;
    private final UploadService uploadService;
    private final RoomService roomService;
    private final UserService userService;

    @GetMapping("")
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

    @GetMapping("/{id}")
    public String getDetailRequest(Model model, @PathVariable("id") Long id) {
        MaintenanceRequest request = maintenanceService.getMaintenanceRequestById(id);
        model.addAttribute("request", request);
        return "admin/maintenance/detail";
    }

    @GetMapping("/create")
    public String getCreateMaintenancePage(Model model) {
        model.addAttribute("newRequest", new MaintenanceRequest());
        model.addAttribute("listBranches", this.branchService.getAllBranches());
        return "admin/maintenance/create";
    }

    @GetMapping("/rooms-by-branch")
    @ResponseBody
    public List<RoomDTO> getRoomsByBranch(@RequestParam("branchId") Long branchId) {
        List<RoomDTO> roomDTOs = roomService.getRoomDTOsByBranchId(branchId);
        return roomDTOs;
    }

    @PostMapping("/create")
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
        if (maintenance.getRoom() != null) {
            if (maintenance.getRoom().getRoomId() == null)
                maintenance.setRoom(null);
        }
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute("id");
        User user = userService.getUserByUserId(userId);
        maintenance.setEmployee(user.getEmployee());
        maintenance.setStatus(MaintenanceStatus.PENDING);
        this.maintenanceService.handleSaveMaintenanceRequest(maintenance);
        return "redirect:/admin/maintenance";
    }

    @GetMapping("/can-update/{id}")
    public ResponseEntity<Boolean> canUpdateRequest(@PathVariable Long id) {
        boolean canUpdate = maintenanceService.canUpdateAndDeleteRequest(id);
        return ResponseEntity.ok(canUpdate);
    }

    @GetMapping("/update/{id}")
    public String getUpdateMaintenancePage(Model model, @PathVariable Long id) {
        MaintenanceRequest request = maintenanceService.getMaintenanceRequestById(id);
        boolean canUpdate = maintenanceService.canUpdateAndDeleteRequest(id);
        model.addAttribute("request", request);
        if (!canUpdate) {
            model.addAttribute("canUpdate", false);
            return "admin/maintenance/update";
        }
        model.addAttribute("listBranches", this.branchService.getAllBranches());
        List<RoomDTO> listRooms = roomService.getRoomDTOsByBranchId(request.getBranch().getBranchId());
        model.addAttribute("listRooms", listRooms);
        return "admin/maintenance/update";
    }

    @PostMapping("/update")
    public String postUpdateBranch(Model model,
            @ModelAttribute("request") @Valid MaintenanceRequest maintenanceRequest,
            BindingResult result,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        MaintenanceRequest currentRequest = this.maintenanceService
                .getMaintenanceRequestById(maintenanceRequest.getRequestId());
        if (result.hasErrors()) {
            return "admin/maintenance/update";
        }
        if (!file.isEmpty()) {
            String img = this.uploadService.handleSaveUploadFile(file, "maintenance");
            currentRequest.setImage(img);
        }
        if (maintenanceRequest.getRoom() != null && maintenanceRequest.getRoom().getRoomId() == null) {
            currentRequest.setRoom(null);
        }
        currentRequest.setBranch(maintenanceRequest.getBranch());
        currentRequest.setStatus(maintenanceRequest.getStatus());
        currentRequest.setDescription(maintenanceRequest.getDescription());
        currentRequest.setUpdatedAt(LocalDateTime.now());
        this.maintenanceService.handleSaveMaintenanceRequest(currentRequest);
        return "redirect:/admin/maintenance";
    }

    @PostMapping("/update-status")
    @ResponseBody
    public ResponseEntity<?> updateMaintenanceStatus(
            @RequestParam("requestId") Long requestId,
            @RequestParam("status") String status) {
        MaintenanceRequest maintenance = maintenanceService.getMaintenanceRequestById(requestId);
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

    @GetMapping("/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteRequest(@PathVariable long id) {
        boolean canDelete = maintenanceService.canUpdateAndDeleteRequest(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/delete")
    public String postDeleteRequest(@RequestParam("requestId") Long requestId) {
        this.maintenanceService.deleteByMaintenanceRequestId(requestId);
        return "redirect:/admin/maintenance";
    }
}
