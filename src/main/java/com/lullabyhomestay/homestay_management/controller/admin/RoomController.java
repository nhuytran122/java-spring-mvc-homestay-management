package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lullabyhomestay.homestay_management.domain.Branch;
import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.service.AmenityService;
import com.lullabyhomestay.homestay_management.service.BranchService;
import com.lullabyhomestay.homestay_management.service.RoomAmenityService;
import com.lullabyhomestay.homestay_management.service.RoomPhotoService;
import com.lullabyhomestay.homestay_management.service.RoomService;
import com.lullabyhomestay.homestay_management.service.RoomTypeService;
import com.lullabyhomestay.homestay_management.service.UploadService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class RoomController {
    private final RoomService roomService;
    private final RoomTypeService roomTypeService;
    private final BranchService branchService;
    private final UploadService uploadService;
    private final RoomPhotoService photoService;
    private final RoomAmenityService roomAmenityService;
    private final AmenityService amenityService;

    @GetMapping("/admin/room")
    public String getProducts(Model model,
            @RequestParam("page") Optional<String> pageOptional) {
        int page = 1;
        try {
            if (pageOptional.isPresent()) {
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

    @GetMapping("/admin/room/{id}")
    public String getDetailRoomPage(Model model, @PathVariable long id) {
        Optional<Room> room = roomService.getRoomByID(id);
        if (!room.isPresent()) {
            return "admin/room";
        }
        model.addAttribute("room", room.get());
        return "admin/room/detail";
    }

    @GetMapping("/admin/room/create")
    public String getCreateRoomPage(Model model) {
        model.addAttribute("newRoom", new Room());
        model.addAttribute("listBranches", this.branchService.getAllBranches());
        model.addAttribute("listRoomTypes", this.roomTypeService.getAllRoomTypes());
        return "admin/room/create";
    }

    @PostMapping("/admin/room/create")
    public String postCreateRoom(Model model,
            @ModelAttribute("newRoom") @Valid Room room,
            BindingResult newRoomBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

        if (newRoomBindingResult.hasErrors()) {
            model.addAttribute("listBranches", this.branchService.getAllBranches());
            model.addAttribute("listRoomTypes", this.roomTypeService.getAllRoomTypes());
            return "admin/room/create";
        }
        this.roomService.handleSaveRoom(room);
        return "redirect:/admin/update";
    }

    @GetMapping("/admin/room/update/{id}")
    public String getUpdateRoomPage(Model model, @PathVariable long id) {
        Optional<Room> room = roomService.getRoomByID(id);
        if (!room.isPresent()) {
            return "admin/room";
        }
        model.addAttribute("room", room.get());
        model.addAttribute("listBranches", this.branchService.getAllBranches());
        model.addAttribute("listRoomTypes", this.roomTypeService.getAllRoomTypes());
        model.addAttribute("listAmenitiesNotInRoom", this.amenityService.getAmenitiesNotInRoom(id));
        return "admin/room/update";
    }

    @PostMapping("/admin/room/update")
    public String postUpdateRoom(Model model,
            @ModelAttribute("room") @Valid Room room,
            BindingResult newRoomBindingResult,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        Long roomID = room.getRoomID();
        Room currentRoom = this.roomService.getRoomByID(roomID).get();
        if (room.getBranch().getBranchID() == 0) {
            newRoomBindingResult.rejectValue("branch",
                    "error.branch", "Vui lòng chọn chi nhánh");
        }
        if (room.getRoomType().getRoomTypeID() == 0) {
            newRoomBindingResult.rejectValue("roomType",
                    "error.roomType", "Vui lòng chọn loại phòng");
        }
        if (newRoomBindingResult.hasErrors()) {
            model.addAttribute("listBranches", this.branchService.getAllBranches());
            model.addAttribute("listRoomTypes", this.roomTypeService.getAllRoomTypes());
            model.addAttribute("listAmenitiesNotInRoom", this.amenityService.getAmenitiesNotInRoom(roomID));
            return "admin/room/update";
        }
        if (currentRoom != null) {
            if (!file.isEmpty()) {
                String img = this.uploadService.handleSaveUploadFile(file, "room");
                currentRoom.setThumbnail(img);
            }
            currentRoom.setRoomNumber(room.getRoomNumber());
            currentRoom.setRoomType(room.getRoomType());
            currentRoom.setBranch(room.getBranch());
            currentRoom.setArea(room.getArea());
            currentRoom.setDescription(room.getDescription());
            this.roomService.handleSaveRoom(currentRoom);
        }
        return "redirect:/admin/room";
    }

    @GetMapping("/admin/room/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteRoom(@PathVariable long id) {
        boolean canDelete = roomService.canDeleteRoom(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/admin/room/delete")
    public String postDeleteRoom(@RequestParam("roomID") long roomID) {
        this.roomService.deleteByRoomID(roomID);
        return "redirect:/admin/room";
    }

}
