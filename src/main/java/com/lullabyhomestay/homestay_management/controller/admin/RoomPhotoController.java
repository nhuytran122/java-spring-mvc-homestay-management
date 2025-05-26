package com.lullabyhomestay.homestay_management.controller.admin;

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
import com.lullabyhomestay.homestay_management.domain.RoomPhoto;
import com.lullabyhomestay.homestay_management.service.RoomPhotoService;
import com.lullabyhomestay.homestay_management.service.RoomService;
import com.lullabyhomestay.homestay_management.service.UploadService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
@RequestMapping("/admin/room/photo")
public class RoomPhotoController {
    private final RoomPhotoService roomPhotoService;
    private final UploadService uploadService;
    private final RoomService roomService;

    @GetMapping("/create")
    public String getCreateRoomPhotoPage(Model model, @RequestParam long roomID) {
        model.addAttribute("newRoomPhoto", new RoomPhoto());
        model.addAttribute("roomID", roomID);
        return "admin/room/room-photo/create";
    }

    @PostMapping("/create")
    public String postCreateRoomPhoto(Model model,
            @ModelAttribute("newRoomPhoto") @Valid RoomPhoto roomPhoto,
            BindingResult newRoomPhotoBindingResult,
            @RequestParam("fileImg") MultipartFile file,
            @RequestParam("roomID") long roomID,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        if (file.isEmpty()) {
            newRoomPhotoBindingResult.rejectValue("photo", "error.photo", "Vui lòng upload ảnh");
        }

        if (newRoomPhotoBindingResult.hasErrors()) {
            model.addAttribute("roomID", roomID);
            return "admin/room/room-photo/create";
        }
        String img;
        if (!file.isEmpty()) {
            img = this.uploadService.handleSaveUploadFile(file, "room");
            roomPhoto.setPhoto(img);
        }
        Room room = roomService.getRoomByID(roomID);
        roomPhoto.setRoom(room);
        this.roomPhotoService.handleSaveRoomPhoto(roomPhoto);
        return "redirect:/admin/room/update/" + roomID;
    }

    @GetMapping("/update/{id}")
    public String getUpdateRoomPhotoPage(Model model, @PathVariable long id) {
        RoomPhoto roomPhoto = roomPhotoService.getPhotoByPhotoID(id);
        model.addAttribute("roomPhoto", roomPhoto);
        return "admin/room/room-photo/update";
    }

    @PostMapping("/update")
    public String postUpdateRoomPhoto(Model model,
            @ModelAttribute("roomPhoto") @Valid RoomPhoto roomPhoto,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {
        // HttpSession session = request.getSession(false);
        Long roomPhotoID = roomPhoto.getPhotoID();
        RoomPhoto currentRoomPhoto = this.roomPhotoService.getPhotoByPhotoID(roomPhotoID);
        if (!file.isEmpty()) {
            String img = this.uploadService.handleSaveUploadFile(file, "room");
            currentRoomPhoto.setPhoto(img);
        }
        currentRoomPhoto.setHidden(roomPhoto.isHidden());
        this.roomPhotoService.handleSaveRoomPhoto(currentRoomPhoto);

        return "redirect:/admin/room/update/" + currentRoomPhoto.getRoom().getRoomID();
    }

    @PostMapping("/delete")
    public String postDeleteRoomPhoto(@RequestParam("photoID") long roomPhotoID) {
        RoomPhoto roomPhoto = roomPhotoService.getPhotoByPhotoID(roomPhotoID);
        this.roomPhotoService.deleteByPhotoID(roomPhotoID);
        return "redirect:/admin/room/update/" + roomPhoto.getRoom().getRoomID();
    }
}
