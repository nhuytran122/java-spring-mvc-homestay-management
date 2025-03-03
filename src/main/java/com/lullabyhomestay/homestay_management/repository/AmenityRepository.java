package com.lullabyhomestay.homestay_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.Amenity;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long>, JpaSpecificationExecutor<Amenity> {
    List<Amenity> findAll();

    Page<Amenity> findAll(Pageable page);

    Optional<Amenity> findByAmenityID(long amenityID);

    Amenity save(Amenity amenity);

    void deleteByAmenityID(long amenityID);

    @Query("SELECT a FROM Amenity a WHERE a.amenityID NOT IN " +
            "(SELECT ra.amenity.amenityID FROM RoomAmenity ra WHERE ra.room.roomID = :roomId)")
    List<Amenity> findAmenitiesNotInRoom(@Param("roomId") long roomId);

    Page<Amenity> findAll(Specification<Amenity> spec, Pageable page);

    boolean existsByAmenityCategory_CategoryID(long categoryID);
}
