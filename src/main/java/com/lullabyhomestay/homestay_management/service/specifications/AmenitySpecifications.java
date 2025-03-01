package com.lullabyhomestay.homestay_management.service.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.lullabyhomestay.homestay_management.domain.Amenity;
import com.lullabyhomestay.homestay_management.domain.AmenityCategory_;
import com.lullabyhomestay.homestay_management.domain.Amenity_;

public class AmenitySpecifications {
    public static Specification<Amenity> nameLike(String keyword) {
        return BaseSpecifications.like(Amenity_.AMENITY_NAME, keyword);
    }

    public static Specification<Amenity> hasCategory(Long categoryID) {
        return BaseSpecifications.equalJoin(Amenity_.AMENITY_CATEGORY, AmenityCategory_.CATEGORY_ID, categoryID);
    }
}