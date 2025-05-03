package com.lullabyhomestay.homestay_management.service.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.lullabyhomestay.homestay_management.domain.Employee;
import com.lullabyhomestay.homestay_management.domain.Employee_;
import com.lullabyhomestay.homestay_management.domain.Role_;
import com.lullabyhomestay.homestay_management.domain.User_;

public class EmployeeSpecifications {
    public static Specification<Employee> hasRole(Long roleID) {
        return BaseSpecifications.equalJoinTwoLevels(Employee_.USER, User_.ROLE, Role_.ROLE_ID, roleID);
    }

    public static Specification<Employee> nameLike(String keyword) {
        return BaseSpecifications.likeJoin(Employee_.USER, User_.FULL_NAME, keyword);
    }

    public static Specification<Employee> addressLike(String keyword) {
        return BaseSpecifications.likeJoin(Employee_.USER, User_.ADDRESS, keyword);
    }

    public static Specification<Employee> emailEqual(String keyword) {
        return BaseSpecifications.equalJoin(Employee_.USER, User_.EMAIL, keyword);
    }

    public static Specification<Employee> phoneEqual(String keyword) {
        return BaseSpecifications.equalJoin(Employee_.USER, User_.PHONE, keyword);
    }
}
