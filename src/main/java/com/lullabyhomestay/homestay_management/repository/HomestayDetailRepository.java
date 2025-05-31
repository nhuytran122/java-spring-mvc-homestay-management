package com.lullabyhomestay.homestay_management.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.HomestayDetail;

@Repository
public interface HomestayDetailRepository extends JpaRepository<HomestayDetail, Long> {

    Page<HomestayDetail> findAll(Pageable page);

    Page<HomestayDetail> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String title, String description, Pageable pageable);

    Optional<HomestayDetail> findByInforId(long inforId);

    HomestayDetail save(HomestayDetail infor);

    void deleteByInforId(long id);

}
