package com.lullabyhomestay.homestay_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

        List<Employee> findAll();

        Page<Employee> findAll(Pageable page);

        Page<Employee> findAll(Specification<Employee> spec, Pageable page);

        Optional<Employee> findByEmployeeID(long employeeID);

        Employee save(Employee employee);

        void deleteByEmployeeID(long employeeID);

        boolean existsByRole_RoleID(long roleID);

        @Query("SELECT e FROM Employee e " +
                        "WHERE (:keyword IS NULL OR " +
                        "       e.fullName LIKE %:keyword% OR " +
                        "       e.phone LIKE %:keyword% OR " +
                        "       e.address LIKE %:keyword% OR " +
                        "       e.email LIKE %:keyword%) " +
                        "AND (:isWorking IS NULL OR e.isWorking = :isWorking) " +
                        "AND (:roleId IS NULL OR e.role.id = :roleId)")
        Page<Employee> searchEmployees(
                        @Param("keyword") String keyword,
                        @Param("isWorking") Boolean isWorking,
                        @Param("roleId") Long roleId,
                        Pageable pageable);

}
