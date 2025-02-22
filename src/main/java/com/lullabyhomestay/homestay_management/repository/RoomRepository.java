package com.lullabyhomestay.homestay_management.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.lullabyhomestay.homestay_management.domain.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    List<Room> findAll();

    Page<Room> findAll(Pageable page);

    boolean existsByBranch_BranchID(long branchID);

    boolean existsByRoomType_RoomTypeID(long roomType);

}
