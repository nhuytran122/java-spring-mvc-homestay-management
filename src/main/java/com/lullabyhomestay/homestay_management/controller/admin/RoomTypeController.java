package com.lullabyhomestay.homestay_management.controller.admin;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.lullabyhomestay.homestay_management.domain.RoomType;
import com.lullabyhomestay.homestay_management.service.RoomTypeService;
import com.lullabyhomestay.homestay_management.service.UploadService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Controller
public class RoomTypeController {
    private final RoomTypeService roomTypeService;
    private final UploadService uploadService;

    @GetMapping("/admin/room-type")
    public String getRoomTypePage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String sort) {
        int validPage = Math.max(1, page);
        Page<RoomType> roomTypes = roomTypeService.searchRoomTypes(keyword,
                validPage, sort);
        List<RoomType> listRoomTypes = roomTypes.getContent();

        StringBuilder extraParams = new StringBuilder();
        if (sort != null) {
            extraParams.append("&sort=").append(sort);
        }
        if (keyword != null && !keyword.isEmpty()) {
            extraParams.append("&keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8));
        }
        model.addAttribute("extraParams", extraParams);

        model.addAttribute("roomTypes", listRoomTypes);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("currentPage", validPage);
        model.addAttribute("totalPages", roomTypes.getTotalPages());
        return "admin/room-type/show";
    }

    @GetMapping("/admin/room-type/{id}")
    public String getDetailRoomType(Model model, @PathVariable long id) {
        RoomType roomType = roomTypeService.getRoomTypeById(id);
        model.addAttribute("roomType", roomType);
        return "admin/room-type/detail";
    }

    @GetMapping("/admin/room-type/create")
    public String getCreateRoomTypePage(Model model) {
        model.addAttribute("newRoomType", new RoomType());
        return "admin/room-type/create";
    }

    @PostMapping("/admin/room-type/create")
    public String postCreateRoomType(Model model,
            @ModelAttribute("newRoomType") @Valid RoomType roomType,
            BindingResult newRoomTypeBindingResult,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
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
        roomTypeService.handleSaveRoomType(roomType);
        return "redirect:/admin/room-type";
    }

    @GetMapping("/admin/room-type/update/{id}")
    public String getUpdateRoomTypePage(Model model, @PathVariable long id) {
        RoomType roomType = roomTypeService.getRoomTypeById(id);
        model.addAttribute("roomType", roomType);
        return "admin/room-type/update";
    }

    @PostMapping("/admin/room-type/update")
    public String postUpdateRoomType(Model model,
            @ModelAttribute("roomType") @Valid RoomType roomType,
            BindingResult roomTypeBindingResult,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
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
        currentRoomType.setPricePerHour(roomType.getPricePerHour());
        currentRoomType.setExtraPricePerHour(roomType.getExtraPricePerHour());

        roomTypeService.handleSaveRoomType(currentRoomType);
        return "redirect:/admin/room-type";
    }

    @GetMapping("/admin/room-type/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteRoomType(@PathVariable long id) {
        boolean canDelete = roomTypeService.canDeleteRoomType(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/admin/room-type/delete")
    public String postDeleteRoomType(@RequestParam("roomTypeID") long roomTypeID) {
        roomTypeService.deleteByRoomTypeID(roomTypeID);
        return "redirect:/admin/room-type";
    }

}