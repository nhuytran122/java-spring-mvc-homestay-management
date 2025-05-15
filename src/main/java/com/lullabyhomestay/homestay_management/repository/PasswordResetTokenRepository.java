package com.lullabyhomestay.homestay_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lullabyhomestay.homestay_management.domain.PasswordResetToken;
import com.lullabyhomestay.homestay_management.domain.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);

    void deleteByUser(User user);
}
