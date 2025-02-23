package com.lullabyhomestay.homestay_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.AmenityCategory;
import com.lullabyhomestay.homestay_management.repository.AmenityCategoryRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AmenityCategoryService {
    private final AmenityCategoryRepository amenityCategoryRepository;

    public List<AmenityCategory> getAllAmenityCategories() {
        return this.amenityCategoryRepository.findAll();
    }

}
