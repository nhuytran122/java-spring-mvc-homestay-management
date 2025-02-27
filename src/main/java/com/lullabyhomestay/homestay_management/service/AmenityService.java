package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.Amenity;
import com.lullabyhomestay.homestay_management.repository.AmenityRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AmenityService {
    private final AmenityRepository amenityRepository;

    public List<Amenity> getAllAmenities() {
        return this.amenityRepository.findAll();
    }

    public Page<Amenity> getAllAmenities(Pageable pageable) {
        return this.amenityRepository.findAll(pageable);
    }

    // TODO: Search theo keyword, categoryID
    public Page<Amenity> searchAmenities(Optional<String> keyword, Optional<Long> categoryID,
            Pageable pageable) {
        return amenityRepository
                .findByAmenityNameContainingIgnoreCaseAndAmenityCategory_CategoryIDOrderByAmenityCategory_CategoryIDDesc(
                        keyword.get(), categoryID.get(), pageable);
    }

    public void handleSaveAmenity(Amenity amenity) {
        this.amenityRepository.save(amenity);
    }

    public Optional<Amenity> getAmenityByID(long amenityID) {
        return this.amenityRepository.findByAmenityID(amenityID);
    }

    @Transactional
    public void deleteByAmenityID(long amenityID) {
        this.amenityRepository.deleteByAmenityID(amenityID);
    }

    public List<Amenity> getAmenitiesNotInRoom(long roomId) {
        return amenityRepository.findAmenitiesNotInRoom(roomId);
    }
}
