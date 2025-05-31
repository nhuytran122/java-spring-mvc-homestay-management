package com.lullabyhomestay.homestay_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.RoomAmenity;
import com.lullabyhomestay.homestay_management.domain.id.RoomAmenityId;

@Repository
public interface RoomAmenityRepository extends JpaRepository<RoomAmenity, RoomAmenityId> {

    RoomAmenity save(RoomAmenity amenity);

    List<RoomAmenity> findByRoom_RoomId(long roomId);

    void deleteByRoom_RoomId(long roomId);

    void deleteByAmenity_AmenityId(long amenityId);

    Optional<RoomAmenity> findByRoomAmenityId(RoomAmenityId id);

    void deleteByRoomAmenityId(RoomAmenityId id);

}
