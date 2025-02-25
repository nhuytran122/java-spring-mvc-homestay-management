package com.lullabyhomestay.homestay_management.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.HomestayDetail;
import com.lullabyhomestay.homestay_management.repository.HomestayDetailRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class HomestayService {
    private final HomestayDetailRepository homestayDetailRepository;

    public Page<HomestayDetail> getAllInforHomestay(Pageable pageable) {
        return this.homestayDetailRepository.findAll(pageable);
    }

    public Page<HomestayDetail> searchInforHomestay(Optional<String> keyword, Pageable pageable) {
        return (keyword.isPresent())
                ? homestayDetailRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        keyword.get(), keyword.get(), pageable)
                : homestayDetailRepository
                        .findAll(pageable);
    }

    public void handleSaveInforHomestay(HomestayDetail infor) {
        this.homestayDetailRepository.save(infor);
    }

    public Optional<HomestayDetail> getInforHomestayByInforID(long inforID) {
        return this.homestayDetailRepository.findByInforID(inforID);
    }

    @Transactional
    public void deleteByInforID(long inforID) {
        this.homestayDetailRepository.deleteByInforID(inforID);
    }
}
