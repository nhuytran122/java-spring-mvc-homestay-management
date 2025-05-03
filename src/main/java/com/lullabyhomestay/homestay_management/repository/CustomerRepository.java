package com.lullabyhomestay.homestay_management.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Page<Customer> findAll(Pageable page);

    Optional<Customer> findByCustomerID(long customerID);

    Customer save(Customer customer);

    void deleteByCustomerID(Long customerID);

    Page<Customer> findAll(Specification<Customer> spec, Pageable page);

    boolean existsByCustomerType_CustomerTypeID(long typeID);
}
