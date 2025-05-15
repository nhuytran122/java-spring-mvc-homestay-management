package com.lullabyhomestay.homestay_management.repository;

import com.lullabyhomestay.homestay_management.domain.EmailVerificationToken;
import com.lullabyhomestay.homestay_management.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    EmailVerificationToken findByToken(String token);

    void deleteByUser(User user);
}