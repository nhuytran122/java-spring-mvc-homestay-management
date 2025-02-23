package com.lullabyhomestay.homestay_management.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.Amenity;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    Page<Amenity> findAll(Pageable page);

    Page<Amenity> findByAmenityNameContainingIgnoreCaseAndAmenityCategory_CategoryIDOrderByAmenityCategory_CategoryIDDesc
    (
            String amenityName, long categoryID, Pageable pageable);

    Optional<Amenity> findByAmenityID(long amenityID);

    Amenity save(Amenity amenity);

    void deleteByAmenityID(long amenityID);

}
