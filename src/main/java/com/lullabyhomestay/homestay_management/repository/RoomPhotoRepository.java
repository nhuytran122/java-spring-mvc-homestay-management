package com.lullabyhomestay.homestay_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lullabyhomestay.homestay_management.domain.RoomPhoto;

public interface RoomPhotoRepository extends JpaRepository<RoomPhoto, Long> {

    Optional<RoomPhoto> findByPhotoId(long photoId);

    List<RoomPhoto> findByRoom_RoomId(long roomId);

    RoomPhoto save(RoomPhoto RoomPhoto);

    void deleteByPhotoId(long id);

    void deleteByRoom_RoomId(long roomId);

}