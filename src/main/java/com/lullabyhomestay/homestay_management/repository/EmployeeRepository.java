package com.lullabyhomestay.homestay_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

        List<Employee> findAll();

        Page<Employee> findAll(Pageable page);

        Page<Employee> findAll(Specification<Employee> spec, Pageable page);

        @EntityGraph(attributePaths = { "user" })
        Optional<Employee> findByEmployeeId(long employeeId);

        Optional<Employee> findByUser_UserId(long userId);

        Employee save(Employee employee);

        void deleteByEmployeeId(long employeeId);
}
