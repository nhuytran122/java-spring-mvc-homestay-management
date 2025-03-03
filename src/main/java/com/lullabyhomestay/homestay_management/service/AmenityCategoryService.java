package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.AmenityCategory;
import com.lullabyhomestay.homestay_management.repository.AmenityCategoryRepository;
import com.lullabyhomestay.homestay_management.repository.AmenityRepository;
import com.lullabyhomestay.homestay_management.utils.Constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AmenityCategoryService {
    private final AmenityCategoryRepository amenityCategoryRepository;
    private final AmenityRepository amenityRepository;

    public List<AmenityCategory> getAllAmenityCategories() {
        return this.amenityCategoryRepository.findAll();
    }

    public Optional<AmenityCategory> getAmenityCategoryByID(long id) {
        return this.amenityCategoryRepository.findByCategoryID(id);
    }

    public AmenityCategory handleSaveAmenityCategory(AmenityCategory amenityCategory) {
        return this.amenityCategoryRepository.save(amenityCategory);
    }

    public Page<AmenityCategory> searchAmenityCategories(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE);
        return (keyword != null && !keyword.isEmpty())
                ? amenityCategoryRepository.findByCategoryNameContainingIgnoreCase(keyword, pageable)
                : amenityCategoryRepository.findAll(pageable);
    }

    public boolean canDeleteCategory(long categoryID) {
        boolean hasAmenity = amenityRepository.existsByAmenityCategory_CategoryID(categoryID);
        return !hasAmenity;
    }

    @Transactional
    public void deleteByCategoryID(long categoryID) {
        if (canDeleteCategory(categoryID)) {
            this.amenityCategoryRepository.deleteByCategoryID(categoryID);
        }
    }
}