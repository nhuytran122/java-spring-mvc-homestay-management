package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.HomestayDetail;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.HomestayDetailRepository;
import com.lullabyhomestay.homestay_management.utils.Constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class HomestayInforService {
    private final HomestayDetailRepository homestayDetailRepository;

    public List<HomestayDetail> getAllInforHomestay() {
        return this.homestayDetailRepository.findAll();
    }

    public Page<HomestayDetail> getAllInforHomestay(Pageable pageable) {
        return this.homestayDetailRepository.findAll(pageable);
    }

    public Page<HomestayDetail> searchInforHomestay(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE);
        return (keyword != null && !keyword.isEmpty())
                ? homestayDetailRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        keyword, keyword, pageable)
                : homestayDetailRepository
                        .findAll(pageable);
    }

    public void handleSaveInforHomestay(HomestayDetail infor) {
        this.homestayDetailRepository.save(infor);
    }

    public HomestayDetail getInforHomestayByInforId(long inforId) {
        Optional<HomestayDetail> infOpt = homestayDetailRepository.findByInforId(inforId);
        if (!infOpt.isPresent()) {
            throw new NotFoundException("Thông tin");
        }
        return infOpt.get();
    }

    @Transactional
    public void deleteByInforId(long inforId) {
        this.homestayDetailRepository.deleteByInforId(inforId);
    }
}
