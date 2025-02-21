package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.service.RoomService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/admin/room")
    public String getProducts(Model model,
            @RequestParam("page") Optional<String> pageOptional) {
        int page = 1;
        try {
            if(pageOptional.isPresent()){
                page = Integer.parseInt(pageOptional.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Pageable pageable = PageRequest.of(page - 1, 2);
        Page<Room> rooms = this.roomService.getAllRooms(pageable);

        List<Room> listRooms = rooms.getContent();
        model.addAttribute("rooms", listRooms);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", rooms.getTotalPages());
        return "admin/room/show";
    }
}
