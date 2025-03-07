package com.lullabyhomestay.homestay_management.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.MaintenanceRequest;
import com.lullabyhomestay.homestay_management.domain.dto.SearchMaintenanceCriteriaDTO;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.MaintenanceRequestRepository;
import com.lullabyhomestay.homestay_management.service.specifications.MaintenanceSpecifications;
import com.lullabyhomestay.homestay_management.utils.Constants;
import com.lullabyhomestay.homestay_management.utils.MaintenanceStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MaintenanceRequestService {
    private final MaintenanceRequestRepository maintenanceRepository;

    public Page<MaintenanceRequest> getAllAmenities(Pageable pageable) {
        return this.maintenanceRepository.findAll(pageable);
    }

    // todo: searchBy Description, Status, orderBy CreatedAt
    // Page<MaintainanceRequest> findBy..(
    // String name, String address, Pageable pageable);

    public Page<MaintenanceRequest> searchMaintenances(SearchMaintenanceCriteriaDTO criteria,
            int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE,
                "asc".equals(criteria.getSort()) ? Sort.by("CreatedAt").ascending()
                        : "desc".equals(criteria.getSort()) ? Sort.by("CreatedAt").descending() : Sort.unsorted());

        if ((criteria.getKeyword() == null || criteria.getKeyword().isEmpty()) && criteria.getBranchID() == null
                && criteria.getStatus() == null || criteria.getStatus().isEmpty())
            return maintenanceRepository
                    .findAll(pageable);
        MaintenanceStatus statusNum = null;
        if (criteria.getStatus() != null && !criteria.getStatus().isEmpty()) {
            try {
                statusNum = MaintenanceStatus.valueOf(criteria.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                statusNum = null;
            }
        }
        Specification<MaintenanceRequest> spec = Specification
                .where(MaintenanceSpecifications.hasBranch(criteria.getBranchID()))
                .and(MaintenanceSpecifications.descriptionLike(criteria.getKeyword()))
                .and(MaintenanceSpecifications.statusEqual(statusNum));
        return maintenanceRepository.findAll(spec, pageable);
    }

    public boolean canUpdateRequest(Long requestID) {
        Optional<MaintenanceRequest> requestOpt = maintenanceRepository.findByRequestID(requestID);
        if (!requestOpt.isPresent()) {
            throw new NotFoundException("Yêu cầu bảo trì");
        }
        MaintenanceRequest transaction = requestOpt.get();
        if (transaction.getStatus() != MaintenanceStatus.PENDING)
            return false;
        return true;
    }

    public void handleSaveMaintenanceRequest(MaintenanceRequest request) {
        this.maintenanceRepository.save(request);
    }

    public MaintenanceRequest getMaintenanceRequestByID(long requestID) {
        Optional<MaintenanceRequest> requestOpt = maintenanceRepository.findByRequestID(requestID);
        if (!requestOpt.isPresent()) {
            throw new NotFoundException("Yêu cầu bảo trì");
        }
        return requestOpt.get();
    }

    @Transactional
    public void deleteByMaintenanceRequestID(long requestID) {
        this.maintenanceRepository.deleteByRequestID(requestID);
    }
}
