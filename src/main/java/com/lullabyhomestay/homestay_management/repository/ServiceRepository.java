package com.lullabyhomestay.homestay_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findAll();

    Page<Service> findAll(Pageable page);

    Page<Service> findByServiceNameContainingIgnoreCase(
            String serviceName, Pageable pageable);

    Optional<Service> findByServiceID(long serviceID);

    Service save(Service service);

    void deleteByServiceID(long serviceID);
}
