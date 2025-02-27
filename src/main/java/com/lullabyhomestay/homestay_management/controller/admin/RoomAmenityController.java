package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lullabyhomestay.homestay_management.domain.Amenity;
import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.domain.RoomAmenity;
import com.lullabyhomestay.homestay_management.domain.RoomAmenityID;
import com.lullabyhomestay.homestay_management.service.AmenityService;
import com.lullabyhomestay.homestay_management.service.RoomAmenityService;
import com.lullabyhomestay.homestay_management.service.RoomService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class RoomAmenityController {
    private final RoomAmenityService roomAmenityService;
    private final RoomService roomService;
    private final AmenityService amenityService;

    @PostMapping("/admin/room/room-amenity/create")
    @ResponseBody
    public ResponseEntity<?> postCreateRoomAmenities(@RequestBody List<RoomAmenity> listRoomAmenities) {
        for (RoomAmenity request : listRoomAmenities) {
            RoomAmenity roomAmenity = new RoomAmenity();
            long roomId = request.getRoomAmenityID().getRoomID();
            long amenityID = request.getRoomAmenityID().getAmenityID();
            RoomAmenityID id = new RoomAmenityID(roomId, amenityID);

            Optional<Room> room = roomService.getRoomByID(roomId);
            Optional<Amenity> amenity = amenityService.getAmenityByID(amenityID);

            if (room.isEmpty() || amenity.isEmpty()) {
                return ResponseEntity.badRequest().body("Room hoặc Amenity không tồn tại");
            }

            roomAmenity.setRoomAmenityID(id);
            roomAmenity.setQuantity(request.getQuantity());
            roomAmenity.setRoom(room.get());
            roomAmenity.setAmenity(amenity.get());
            this.roomAmenityService.handleSaveRoomAmenity(roomAmenity);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/room/room-amenity/update")
    @ResponseBody
    public ResponseEntity<?> postUpdateRoomAmenity(@RequestBody RoomAmenity roomAmenity) {
        long roomID = roomAmenity.getRoomAmenityID().getRoomID();
        RoomAmenity currentRoomAmenity = (this.roomAmenityService.getRoomAmenityByID(
                roomID, roomAmenity.getRoomAmenityID().getAmenityID())).get();
        if (currentRoomAmenity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room amenity not found");
        }
        Optional<Room> room = roomService.getRoomByID(roomID);
        Optional<Amenity> amenity = amenityService.getAmenityByID(roomAmenity.getRoomAmenityID().getAmenityID());

        currentRoomAmenity.setQuantity(roomAmenity.getQuantity());
        roomAmenity.setRoom(room.get());
        roomAmenity.setAmenity(amenity.get());
        this.roomAmenityService.handleSaveRoomAmenity(currentRoomAmenity);
        // return "redirect:/admin/room/update/room-amenity/" + roomID;
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/room/room-amenity/delete")
    public String postHiddenRoomAmenity(Model model,
            @ModelAttribute("roomAmenity") RoomAmenity roomAmenity) {
        long roomID = roomAmenity.getRoomAmenityID().getRoomID();
        RoomAmenity currentRoomAmenity = (this.roomAmenityService.getRoomAmenityByID(
                roomID, roomAmenity.getRoomAmenityID().getAmenityID())).get();
        if (currentRoomAmenity == null) {
            return "redirect:/admin/room/update/" + roomID;
        }
        this.roomAmenityService.deleteByRoomAmenityID(roomID, currentRoomAmenity.getRoomAmenityID().getAmenityID());
        return "redirect:/admin/room/update/" + roomID;
    }
}
