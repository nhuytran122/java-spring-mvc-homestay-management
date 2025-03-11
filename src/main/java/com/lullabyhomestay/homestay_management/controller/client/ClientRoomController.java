package com.lullabyhomestay.homestay_management.controller.client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.domain.dto.SearchRoomCriteriaDTO;
import com.lullabyhomestay.homestay_management.service.BranchService;
import com.lullabyhomestay.homestay_management.service.RoomService;
import com.lullabyhomestay.homestay_management.service.RoomTypeService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class ClientRoomController {
    private final RoomService roomService;
    private final RoomTypeService roomTypeService;
    private final BranchService branchService;

    @GetMapping("/room")
    public String getRoomsPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @ModelAttribute SearchRoomCriteriaDTO criteria) {
        int validPage = Math.max(1, page);

        if (criteria.getTimeRange() == null || criteria.getTimeRange().isEmpty()) {
            LocalDateTime startDefault = LocalDateTime.now();
            LocalDateTime endDefault = LocalDateTime.now().plusMonths(2);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            criteria.setTimeRange(startDefault.format(formatter) + " - " + endDefault.format(formatter));
        }
        if (criteria.getFromTime().isAfter(criteria.getToTime())) {
            model.addAttribute("errorMessage", "Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc!");
            return prepareModelWithoutSearch(model, criteria, validPage);
        }
        Page<Room> rooms = roomService.searchRoomsForClient(criteria, validPage);
        List<Room> listRooms = rooms.getContent();
        model.addAttribute("rooms", listRooms);
        model.addAttribute("totalPages", rooms.getTotalPages());
        return prepareModelWithoutSearch(model, criteria, validPage);
    }

    @GetMapping("/room/{id}")
    public String getDetailRoomPage(Model model, @PathVariable long id) {
        Room room = roomService.getRoomByID(id);
        model.addAttribute("room", room);
        return "client/room/detail";
    }

    private String prepareModelWithoutSearch(Model model, SearchRoomCriteriaDTO criteria, int validPage) {
        model.addAttribute("criteria", criteria);
        model.addAttribute("extraParams", criteria.convertToExtraParams());
        model.addAttribute("currentPage", validPage);
        model.addAttribute("listBranches", this.branchService.getAllBranches());
        model.addAttribute("listRoomTypes", this.roomTypeService.getAllRoomTypes());
        return "client/room/show";
    }

}
