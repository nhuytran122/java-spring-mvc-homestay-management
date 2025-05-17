package com.lullabyhomestay.homestay_management.domain;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "PasswordResetTokens")
@Getter
@Setter
public class PasswordResetToken extends VerificationTokenBase {
    public PasswordResetToken() {
        super();
    }

    public PasswordResetToken(String token, User user, LocalDateTime expiryDate) {
        super(token, user, expiryDate);
    }
}
