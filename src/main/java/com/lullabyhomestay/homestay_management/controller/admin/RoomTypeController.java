package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.lullabyhomestay.homestay_management.domain.RoomPricing;
import com.lullabyhomestay.homestay_management.domain.RoomType;
import com.lullabyhomestay.homestay_management.service.RoomPricingService;
import com.lullabyhomestay.homestay_management.service.RoomTypeService;
import com.lullabyhomestay.homestay_management.service.UploadService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Controller
@PreAuthorize("hasAnyRole('MANAGER')")
@RequestMapping("/admin/room-type")
public class RoomTypeController {
    private final RoomTypeService roomTypeService;
    private final UploadService uploadService;
    private final RoomPricingService roomPricingService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    public String getRoomTypePage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword) {
        int validPage = Math.max(1, page);
        Page<RoomType> roomTypes = roomTypeService.searchRoomTypes(keyword,
                validPage);
        List<RoomType> listRoomTypes = roomTypes.getContent();

        model.addAttribute("roomTypes", listRoomTypes);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", validPage);
        model.addAttribute("totalPages", roomTypes.getTotalPages());
        return "admin/room-type/show";
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public String getDetailRoomType(Model model, @PathVariable long id) {
        RoomType roomType = roomTypeService.getRoomTypeById(id);
        model.addAttribute("roomType", roomType);
        return "admin/room-type/detail";
    }

    @GetMapping("/create")
    public String getCreateRoomTypePage(Model model) {
        model.addAttribute("newRoomType", new RoomType());
        return "admin/room-type/create";
    }

    @PostMapping("/create")
    public String postCreateRoomType(Model model,
            @ModelAttribute("newRoomType") @Valid RoomType roomType,
            BindingResult newRoomTypeBindingResult,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {

        if (roomTypeService.existsByName(roomType.getName())) {
            newRoomTypeBindingResult.rejectValue("name", "error.name", "Tên loại phòng đã tồn tại!");
        }
        if (file.isEmpty()) {
            newRoomTypeBindingResult.rejectValue("photo", "error.photo", "Vui lòng upload ảnh");
        }

        if (newRoomTypeBindingResult.hasErrors()) {
            return "admin/room-type/create";
        }
        String img;
        if (!file.isEmpty()) {
            img = this.uploadService.handleSaveUploadFile(file, "room");
            roomType.setPhoto(img);
        }
        roomTypeService.createRoomType(roomType);
        return "redirect:/admin/room-type";
    }

    @GetMapping("/update/{id}")
    public String getUpdateRoomTypePage(Model model, @PathVariable long id) {
        RoomType roomType = roomTypeService.getRoomTypeById(id);
        Optional<RoomPricing> defaultPricingOpt = roomPricingService
                .getDefaultRoomPricing(id);
        RoomPricing defaultPricing;
        if (defaultPricingOpt.isEmpty()) {
            defaultPricing = new RoomPricing();
        } else {
            defaultPricing = defaultPricingOpt.get();
        }
        roomType.setRoomPricings(List.of(defaultPricing));
        model.addAttribute("roomType", roomType);
        return "admin/room-type/update";
    }

    @PostMapping("/update")
    public String postUpdateRoomType(Model model,
            @ModelAttribute("roomType") @Valid RoomType roomType,
            BindingResult roomTypeBindingResult,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {

        if (roomTypeService.existsByNameAndNotId(roomType.getName(), roomType.getRoomTypeID())) {
            roomTypeBindingResult.rejectValue("name", "error.name", "Tên loại phòng đã tồn tại!");
        }
        RoomType currentRoomType = roomTypeService.getRoomTypeById(roomType.getRoomTypeID());
        if (roomTypeBindingResult.hasErrors()) {
            return "admin/room-type/update";
        }
        if (!file.isEmpty()) {
            String img = this.uploadService.handleSaveUploadFile(file, "room");
            currentRoomType.setPhoto(img);
        }
        currentRoomType.setName(roomType.getName());
        currentRoomType.setDescription(roomType.getDescription());
        currentRoomType.setMaxGuest(roomType.getMaxGuest());
        if (roomType.getRoomPricings() != null && !roomType.getRoomPricings().isEmpty()) {
            currentRoomType.setRoomPricings(roomType.getRoomPricings());
        }
        roomTypeService.updateRoomType(currentRoomType);
        return "redirect:/admin/room-type";
    }

    @GetMapping("/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteRoomType(@PathVariable long id) {
        boolean canDelete = roomTypeService.canDeleteRoomType(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/delete")
    public String postDeleteRoomType(@RequestParam("roomTypeID") long roomTypeID) {
        roomTypeService.deleteByRoomTypeID(roomTypeID);
        return "redirect:/admin/room-type";
    }

}