package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.Amenity;
import com.lullabyhomestay.homestay_management.repository.AmenityRepository;
import com.lullabyhomestay.homestay_management.service.specifications.AmenitySpecifications;
import com.lullabyhomestay.homestay_management.utils.Constants;

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

    public Page<Amenity> searchAmenities(String keyword, Long categoryID,
            int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE);

        if ((keyword == null || keyword.isEmpty()) && categoryID == null)
            return amenityRepository
                    .findAll(pageable);
        Specification<Amenity> spec = Specification.where(AmenitySpecifications.hasCategory(categoryID))
                .and(AmenitySpecifications.nameLike(keyword));
        return amenityRepository.findAll(spec, pageable);
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
