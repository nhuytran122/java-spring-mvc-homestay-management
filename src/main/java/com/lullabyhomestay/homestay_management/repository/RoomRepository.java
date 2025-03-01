package com.lullabyhomestay.homestay_management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.lullabyhomestay.homestay_management.domain.Room;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {

    Page<Room> findAll(Pageable page);

    Page<Room> findAll(Specification<Room> spec, Pageable page);

    Optional<Room> findByRoomID(long roomID);

    Room save(Room room);

    void deleteByRoomID(long roomID);

    boolean existsByBranch_BranchID(long branchID);

    boolean existsByRoomType_RoomTypeID(long roomType);

}
