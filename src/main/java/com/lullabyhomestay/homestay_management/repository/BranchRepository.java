package com.lullabyhomestay.homestay_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findAll();

    Page<Branch> findAll(Pageable page);

    Page<Branch> findByBranchNameContainingIgnoreCaseOrAddressContainingIgnoreCase(
    String name, String address, Pageable pageable);

    Optional<Branch> findByBranchID(long branchID);

    Branch save(Branch branch);

    void deleteByBranchID(long id);
}
