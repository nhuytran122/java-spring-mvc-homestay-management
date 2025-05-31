package com.lullabyhomestay.homestay_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Page<Customer> findAll(Pageable page);

    @EntityGraph(attributePaths = { "user" })
    Optional<Customer> findByCustomerId(long customerId);

    Customer save(Customer customer);

    void deleteByCustomerId(Long customerId);

    Page<Customer> findAll(Specification<Customer> spec, Pageable page);

    boolean existsByCustomerType_CustomerTypeId(long typeId);

    // @Query("SELECT COUNT(c) FROM Customer c WHERE c.user.createdAt BETWEEN
    // :startDate AND :endDate")
    // Long countNewCustomers(@Param("startDate") LocalDateTime startDate,
    // @Param("endDate") LocalDateTime endDate);

    @Query("""
                SELECT c
                FROM Customer c
                JOIN c.user u
                ORDER BY c.rewardPoints DESC
            """)
    List<Customer> findTopCustomersByRewardPoints(Pageable pageable);

}
