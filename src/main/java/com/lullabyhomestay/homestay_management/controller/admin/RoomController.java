package com.lullabyhomestay.homestay_management.controller.admin;

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
import org.springframework.web.multipart.MultipartFile;

import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.domain.dto.SearchRoomCriteriaDTO;
import com.lullabyhomestay.homestay_management.service.AmenityService;
import com.lullabyhomestay.homestay_management.service.BranchService;
import com.lullabyhomestay.homestay_management.service.RoomService;
import com.lullabyhomestay.homestay_management.service.RoomTypeService;
import com.lullabyhomestay.homestay_management.service.UploadService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@PreAuthorize("hasAnyRole('MANAGER')")
@RequestMapping("/admin/room")
public class RoomController {
    private final RoomService roomService;
    private final RoomTypeService roomTypeService;
    private final BranchService branchService;
    private final UploadService uploadService;
    private final AmenityService amenityService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    public String getRoomsPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @ModelAttribute SearchRoomCriteriaDTO criteria) {
        int validPage = Math.max(1, page);

        Page<Room> rooms = roomService.searchRooms(criteria, validPage);
        List<Room> listRooms = rooms.getContent();

        model.addAttribute("criteria", criteria);
        model.addAttribute("listBranches", this.branchService.getAllBranches());
        model.addAttribute("listRoomTypes", this.roomTypeService.getAllRoomTypes());
        model.addAttribute("extraParams", criteria.convertToExtraParams());

        model.addAttribute("rooms", listRooms);
        model.addAttribute("currentPage", validPage);
        model.addAttribute("totalPages", rooms.getTotalPages());
        return "admin/room/show";
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public String getDetailRoomPage(Model model, @PathVariable long id) {
        Room room = roomService.getRoomByID(id);
        model.addAttribute("room", room);
        return "admin/room/detail";
    }

    @GetMapping("/create")
    public String getCreateRoomPage(Model model) {
        model.addAttribute("newRoom", new Room());
        model.addAttribute("listBranches", this.branchService.getAllBranches());
        model.addAttribute("listRoomTypes", this.roomTypeService.getAllRoomTypes());
        return "admin/room/create";
    }

    @PostMapping("/create")
    public String postCreateRoom(Model model,
            @ModelAttribute("newRoom") @Valid Room room,
            BindingResult newRoomBindingResult,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        if (newRoomBindingResult.hasErrors()) {
            model.addAttribute("listBranches", this.branchService.getAllBranches());
            model.addAttribute("listRoomTypes", this.roomTypeService.getAllRoomTypes());
            return "admin/room/create";
        }
        if (!file.isEmpty()) {
            String img = this.uploadService.handleSaveUploadFile(file, "room");
            room.setThumbnail(img);
        }
        Room newRoom = this.roomService.handleSaveRoom(room);
        return "redirect:/admin/room/update/" + newRoom.getRoomID();
    }

    @GetMapping("/update/{id}")
    public String getUpdateRoomPage(Model model, @PathVariable long id) {
        Room room = roomService.getRoomByID(id);
        model.addAttribute("room", room);
        model.addAttribute("listBranches", this.branchService.getAllBranches());
        model.addAttribute("listRoomTypes", this.roomTypeService.getAllRoomTypes());
        model.addAttribute("listAmenitiesNotInRoom", this.amenityService.getAmenitiesNotInRoom(id));

        model.addAttribute("listPhotos", room.getRoomPhotos());
        model.addAttribute("listAmenities", room.getRoomAmenities());
        return "admin/room/update";
    }

    @PostMapping("/update")
    public String postUpdateRoom(Model model,
            @ModelAttribute("room") @Valid Room room,
            BindingResult newRoomBindingResult,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        Long roomID = room.getRoomID();
        Room currentRoom = this.roomService.getRoomByID(roomID);
        if (newRoomBindingResult.hasErrors()) {
            model.addAttribute("listBranches", this.branchService.getAllBranches());
            model.addAttribute("listRoomTypes", this.roomTypeService.getAllRoomTypes());
            Room roomWithDetails = this.roomService.getRoomByID(roomID);
            model.addAttribute("listAmenitiesNotInRoom", this.amenityService.getAmenitiesNotInRoom(roomID));

            model.addAttribute("listPhotos", roomWithDetails.getRoomPhotos());
            model.addAttribute("listAmenities", roomWithDetails.getRoomAmenities());
            return "admin/room/update";
        }
        if (!file.isEmpty()) {
            String img = this.uploadService.handleSaveUploadFile(file, "room");
            currentRoom.setThumbnail(img);
        }
        currentRoom.setRoomNumber(room.getRoomNumber());
        currentRoom.setRoomType(room.getRoomType());
        currentRoom.setBranch(room.getBranch());
        currentRoom.setArea(room.getArea());
        currentRoom.setIsActive(room.getIsActive());
        this.roomService.handleSaveRoom(currentRoom);
        return "redirect:/admin/room";
    }

    @GetMapping("/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteRoom(@PathVariable long id) {
        boolean canDelete = roomService.canDeleteRoom(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/delete")
    public String postDeleteRoom(@RequestParam("roomID") long roomID) {
        this.roomService.deleteByRoomID(roomID);
        return "redirect:/admin/room";
    }

}
