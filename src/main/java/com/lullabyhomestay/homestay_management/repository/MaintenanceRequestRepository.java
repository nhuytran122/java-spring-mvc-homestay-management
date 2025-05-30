package com.lullabyhomestay.homestay_management.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.MaintenanceRequest;

@Repository
public interface MaintenanceRequestRepository
        extends JpaRepository<MaintenanceRequest, Long>, JpaSpecificationExecutor<MaintenanceRequest> {
    Page<MaintenanceRequest> findAll(Pageable page);

    Page<MaintenanceRequest> findAll(Specification<MaintenanceRequest> spec, Pageable page);

    Optional<MaintenanceRequest> findByRequestId(long requestId);

    MaintenanceRequest save(MaintenanceRequest request);

    void deleteByRequestId(long id);

    boolean existsByRoom_RoomId(long roomId);

    boolean existsByEmployee_EmployeeId(long employeeId);

    boolean existsByBranch_BranchId(long branchId);

}
