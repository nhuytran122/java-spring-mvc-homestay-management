package com.lullabyhomestay.homestay_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.Refund;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {

}
