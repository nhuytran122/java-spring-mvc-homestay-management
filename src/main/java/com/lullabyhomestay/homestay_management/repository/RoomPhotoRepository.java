package com.lullabyhomestay.homestay_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lullabyhomestay.homestay_management.domain.RoomPhoto;

public interface RoomPhotoRepository extends JpaRepository<RoomPhoto, Long> {

    Optional<RoomPhoto> findByPhotoID(long photoId);

    List<RoomPhoto> findByRoom_RoomID(long roomID);

    RoomPhoto save(RoomPhoto RoomPhoto);

    void deleteByPhotoID(long id);

    void deleteByRoom_RoomID(long roomID);

}