package com.lullabyhomestay.homestay_management.repository;

import com.lullabyhomestay.homestay_management.domain.VerificationToken;
import com.lullabyhomestay.homestay_management.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    void deleteByUser(User user);
}