package com.lullabyhomestay.homestay_management.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.MaintenanceRequest;

@Repository
public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Long> {
    Page<MaintenanceRequest> findAll(Pageable page);

    // todo: searchBy Description, Status, orderBy CreatedAt
    // Page<MaintainanceRequest> findBy..(
    // String name, String address, Pageable pageable);

    Optional<MaintenanceRequest> findByRequestID(long requestID);

    MaintenanceRequest save(MaintenanceRequest request);

    void deleteByRequestID(long id);

    boolean existsByRoom_RoomID(long roomID);

    boolean existsByEmployee_EmployeeID(long employeeID);
}
