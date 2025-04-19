package com.lullabyhomestay.homestay_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.ActionLog;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
    List<ActionLog> findAll();

}
