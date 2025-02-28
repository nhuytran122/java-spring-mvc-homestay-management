package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.lullabyhomestay.homestay_management.domain.RoomType;
import com.lullabyhomestay.homestay_management.service.RoomTypeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    @GetMapping("/admin/room-type")
    public String getRoomTypePage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String sort) {
        int validPage = Math.max(1, page);
        Page<RoomType> roomTypes = roomTypeService.searchRoomTypes(keyword,
                validPage, sort);

        List<RoomType> listRoomTypes = roomTypes.getContent();
        model.addAttribute("roomTypes", listRoomTypes);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", roomTypes.getTotalPages());
        return "admin/room-type/show";
    }

    @GetMapping("/admin/room-type/{id}")
    public String getDetailRoomType(Model model, @PathVariable long id) {
        Optional<RoomType> roomType = roomTypeService.getRoomTypeById(id);
        if (!roomType.isPresent()) {
            return "admin/room-type";
        }
        model.addAttribute("roomType", roomType.get());
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
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        if (newRoomTypeBindingResult.hasErrors()) {
            return "admin/room-type/create";
        }
        roomTypeService.handleSaveRoomType(roomType);
        return "redirect:/admin/room-type";
    }

    @GetMapping("/admin/room-type/update/{id}")
    public String getUpdateRoomTypePage(Model model, @PathVariable long id) {
        Optional<RoomType> roomType = roomTypeService.getRoomTypeById(id);
        if (!roomType.isPresent()) {
            return "admin/room-type";
        }
        model.addAttribute("roomType", roomType.get());
        return "admin/room-type/update";
    }

    @PostMapping("/admin/room-type/update")
    public String postUpdateRoomType(Model model,
            @ModelAttribute("roomType") @Valid RoomType roomType,
            BindingResult roomTypeBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        RoomType currentRoomType = roomTypeService.getRoomTypeById(roomType.getRoomTypeID()).get();
        if (roomTypeBindingResult.hasErrors()) {
            return "admin/room-type/update";
        }

        if (currentRoomType != null) {
            currentRoomType.setName(roomType.getName());
            currentRoomType.setDescription(roomType.getDescription());
            currentRoomType.setMaxGuest(roomType.getMaxGuest());
            currentRoomType.setPricePerHour(roomType.getPricePerHour());
            currentRoomType.setExtraPricePerHour(roomType.getExtraPricePerHour());

            roomTypeService.handleSaveRoomType(currentRoomType);
        }
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