package com.lullabyhomestay.homestay_management.controller.admin;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.RoomPricing;
import com.lullabyhomestay.homestay_management.domain.RoomType;
import com.lullabyhomestay.homestay_management.service.RoomPricingService;
import com.lullabyhomestay.homestay_management.service.RoomTypeService;
import com.lullabyhomestay.homestay_management.service.validator.AdminValidation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@PreAuthorize("hasAnyRole('MANAGER')")
@RequestMapping("/admin/room-pricing")
public class RoomPricingController {
    private final RoomPricingService roomPricingService;
    private final RoomTypeService roomTypeService;

    @GetMapping("/create/{roomTypeID}")
    public String getCreateRoomTypePage(Model model, @PathVariable Long roomTypeID) {
        RoomType roomType = roomTypeService.getRoomTypeById(roomTypeID);
        RoomPricing newRoomPricing = new RoomPricing();
        newRoomPricing.setRoomType(roomType);
        model.addAttribute("newRoomPricing", newRoomPricing);
        return "admin/room-pricing/create";
    }

    @PostMapping("/create")
    public String postCreateRoomPricing(Model model,
            @ModelAttribute("newRoomPricing") @Valid RoomPricing newRoomPricing,
            BindingResult result,
            HttpServletRequest request) {

        LocalDate startDate = newRoomPricing.getStartDate();
        LocalDate endDate = newRoomPricing.getEndDate();
        if (!newRoomPricing.getIsDefault()) {
            if (startDate == null) {
                result.rejectValue("startDate", "error.startDate",
                        "Vui lòng nhập thời gian bắt đầu chính sách giá");
            }
            if (endDate == null) {
                result.rejectValue("endDate", "error.endDate",
                        "Vui lòng nhập thời gian kết thúc chính sách giá");
            }

            if (startDate != null && endDate != null && roomPricingService.isOverlapping(startDate, endDate,
                    newRoomPricing.getRoomType().getRoomTypeID(), newRoomPricing.getRoomPricingID())) {
                String duplicateMessage = "Thời gian áp dụng chính sách giá đang bị trùng lặp";
                result.rejectValue("startDate", "error.startDate",
                        duplicateMessage);
                result.rejectValue("endDate", "error.endDate", duplicateMessage);
            }
        }

        if (result.hasErrors()) {
            return "admin/room-pricing/create";
        }
        roomPricingService.handleSaveRoomPricing(newRoomPricing);
        return "redirect:/admin/room-type/" + newRoomPricing.getRoomType().getRoomTypeID();
    }

    @GetMapping("/update/{id}")
    public String getUpdateRoomTypePage(Model model, @PathVariable Long id) {
        RoomPricing roomPricing = roomPricingService.getRoomPricingByID(id);
        model.addAttribute("roomPricing", roomPricing);
        return "admin/room-pricing/update";
    }

    @PostMapping("/update")
    public String postUpdateRoomType(Model model,
            @ModelAttribute("roomPricing") @Validated(AdminValidation.class) @Valid RoomPricing roomPricing,
            BindingResult result,
            HttpServletRequest request) {
        LocalDate startDate = roomPricing.getStartDate();
        LocalDate endDate = roomPricing.getEndDate();
        if (!roomPricing.getIsDefault()) {
            if (startDate == null) {
                result.rejectValue("startDate", "error.startDate",
                        "Vui lòng nhập thời gian bắt đầu chính sách giá");
            }
            if (endDate == null) {
                result.rejectValue("endDate", "error.endDate",
                        "Vui lòng nhập thời gian kết thúc chính sách giá");
            }

            if (startDate != null && endDate != null && roomPricingService.isOverlapping(startDate, endDate,
                    roomPricing.getRoomType().getRoomTypeID(), roomPricing.getRoomPricingID())) {
                String duplicateMessage = "Thời gian áp dụng chính sách giá đang bị trùng lặp";
                result.rejectValue("startDate", "error.startDate",
                        duplicateMessage);
                result.rejectValue("endDate", "error.endDate", duplicateMessage);
            }
        }
        if (result.hasErrors()) {
            return "admin/room-pricing/update";
        }
        roomPricingService.handleSaveRoomPricing(roomPricing);
        return "redirect:/admin/room-type/" + roomPricing.getRoomType().getRoomTypeID();
    }

    @GetMapping("/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteRoomPricing(@PathVariable Long id) {
        boolean canDelete = roomPricingService.canDeleteRoomPricing(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/delete")
    public String postDeleteRoomType(@RequestParam("roomPricingID") Long roomPricingID) {
        RoomPricing roomPricing = roomPricingService.getRoomPricingByID(roomPricingID);

        roomPricingService.deleteRoomPricingByID(roomPricingID);
        return "redirect:/admin/room-type/" + roomPricing.getRoomType().getRoomTypeID();
    }

}
