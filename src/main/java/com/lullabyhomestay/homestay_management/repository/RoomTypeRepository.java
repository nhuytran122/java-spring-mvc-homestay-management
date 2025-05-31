package com.lullabyhomestay.homestay_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.RoomType;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    List<RoomType> findAll();

    Page<RoomType> findAll(Pageable page);

    Optional<RoomType> findByRoomTypeId(long roomTypeId);

    Page<RoomType> findByNameContainingIgnoreCase(String roomTypeName, Pageable page);

    RoomType save(RoomType roomType);

    void deleteByRoomTypeId(long id);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndRoomTypeIdNot(String name, Long roomTypeId);

}