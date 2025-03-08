package com.lullabyhomestay.homestay_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.CustomerType;

@Repository
public interface CustomerTypeRepository extends JpaRepository<CustomerType, Long> {
    List<CustomerType> findAll();

    Page<CustomerType> findAll(Pageable page);

    Optional<CustomerType> findByCustomerTypeID(Long id);

    Page<CustomerType> findByNameContainingIgnoreCase(String name, Pageable page);

    CustomerType save(CustomerType type);

    void deleteByCustomerTypeID(Long id);

    Optional<CustomerType> findFirstByOrderByMinPointAsc();

}
