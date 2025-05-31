package com.lullabyhomestay.homestay_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.AmenityCategory;

@Repository
public interface AmenityCategoryRepository extends JpaRepository<AmenityCategory, Long> {
    List<AmenityCategory> findAll();

    Page<AmenityCategory> findAll(Pageable page);

    Page<AmenityCategory> findByCategoryNameContainingIgnoreCase(String keyword, Pageable page);

    Optional<AmenityCategory> findByCategoryId(long categoryId);

    AmenityCategory save(AmenityCategory category);

    void deleteByCategoryId(long id);

    boolean existsByCategoryNameIgnoreCase(String name);

    boolean existsByCategoryNameIgnoreCaseAndCategoryIdNot(String name, Long categoryId);
}
