package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lullabyhomestay.homestay_management.domain.Amenity;
import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.domain.RoomAmenity;
import com.lullabyhomestay.homestay_management.domain.id.RoomAmenityId;
import com.lullabyhomestay.homestay_management.service.AmenityService;
import com.lullabyhomestay.homestay_management.service.RoomAmenityService;
import com.lullabyhomestay.homestay_management.service.RoomService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
@RequestMapping("/admin/room/room-amenity")
public class RoomAmenityController {
    private final RoomAmenityService roomAmenityService;
    private final RoomService roomService;
    private final AmenityService amenityService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> postCreateRoomAmenities(@RequestBody List<RoomAmenity> listRoomAmenities) {
        for (RoomAmenity request : listRoomAmenities) {
            RoomAmenity roomAmenity = new RoomAmenity();
            long roomId = request.getRoomAmenityId().getAmenityId();
            long amenityId = request.getRoomAmenityId().getAmenityId();
            RoomAmenityId id = new RoomAmenityId(roomId, amenityId);

            Room room = roomService.getRoomById(roomId);
            Amenity amenity = amenityService.getAmenityById(amenityId);

            roomAmenity.setRoomAmenityId(id);
            roomAmenity.setQuantity(request.getQuantity());
            roomAmenity.setRoom(room);
            roomAmenity.setAmenity(amenity);
            this.roomAmenityService.handleSaveRoomAmenity(roomAmenity);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<?> postUpdateRoomAmenity(@RequestBody RoomAmenity roomAmenity) {
        long roomId = roomAmenity.getRoomAmenityId().getRoomId();
        RoomAmenity currentRoomAmenity = (this.roomAmenityService.getRoomAmenityById(
                roomId, roomAmenity.getRoomAmenityId().getAmenityId()));
        Room room = roomService.getRoomById(roomId);
        Amenity amenity = amenityService.getAmenityById(roomAmenity.getRoomAmenityId().getAmenityId());

        currentRoomAmenity.setQuantity(roomAmenity.getQuantity());
        roomAmenity.setRoom(room);
        roomAmenity.setAmenity(amenity);
        this.roomAmenityService.handleSaveRoomAmenity(currentRoomAmenity);
        // return "redirect:/admin/room/update/room-amenity/" + roomId;
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public String postDeleteRoomAmenity(Model model,
            @ModelAttribute("roomAmenity") RoomAmenity roomAmenity) {
        long roomId = roomAmenity.getRoomAmenityId().getRoomId();
        RoomAmenity currentRoomAmenity = (this.roomAmenityService.getRoomAmenityById(
                roomId, roomAmenity.getRoomAmenityId().getAmenityId()));
        this.roomAmenityService.deleteByRoomAmenityId(roomId, currentRoomAmenity.getRoomAmenityId().getAmenityId());
        return "redirect:/admin/room/update/" + roomId;
    }
}
