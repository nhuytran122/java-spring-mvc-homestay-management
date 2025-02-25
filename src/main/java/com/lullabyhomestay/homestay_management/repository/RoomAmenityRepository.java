package com.lullabyhomestay.homestay_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.RoomAmenity;
import com.lullabyhomestay.homestay_management.domain.RoomAmenityID;

@Repository
public interface RoomAmenityRepository extends JpaRepository<RoomAmenity, RoomAmenityID> {

    RoomAmenity save(RoomAmenity amenity);

    void deleteByRoom_RoomID(long roomID);

    void deleteByAmenity_AmenityID(long amenityID);

}
