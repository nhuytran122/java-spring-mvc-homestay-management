package com.lullabyhomestay.homestay_management.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lullabyhomestay.homestay_management.domain.RoomStatusHistory;

public interface RoomStatusHistoryRepository extends JpaRepository<RoomStatusHistory, Long> {
    Page<RoomStatusHistory> findAll(Pageable page);

    Optional<RoomStatusHistory> findByRoomStatusID(long id);

    Page<RoomStatusHistory> findByRoom_RoomIDAndRoomStatus_StatusID(long roomID, long statusID, Pageable page);

    RoomStatusHistory save(RoomStatusHistory roomStatusHistory);

    void deleteByRoom_RoomID(long id);

}
