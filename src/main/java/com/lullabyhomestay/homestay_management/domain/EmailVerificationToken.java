package com.lullabyhomestay.homestay_management.domain;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "EmailVerificationTokens")
@Getter
@Setter
public class EmailVerificationToken extends VerificationTokenBase {
    public EmailVerificationToken() {
        super();
    }

    public EmailVerificationToken(String token, User user, LocalDateTime expiryDate) {
        super(token, user, expiryDate);
    }
}
