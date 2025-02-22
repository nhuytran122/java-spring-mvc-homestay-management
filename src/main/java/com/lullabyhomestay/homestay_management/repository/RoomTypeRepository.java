package com.lullabyhomestay.homestay_management.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.RoomType;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    Page<RoomType> findAll(Pageable page);

    Optional<RoomType> findByRoomTypeID(long roomTypeId);

    Page<RoomType> findByNameContainingIgnoreCase(String roomTypeName, Pageable page);

    RoomType save(RoomType roomType);

    void deleteByRoomTypeID(long id);

}