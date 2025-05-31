package com.lullabyhomestay.homestay_management.domain;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_tokens")
@Getter
@Setter
public class VerificationToken extends VerificationTokenBase {
    public VerificationToken() {
        super();
    }

    public VerificationToken(String token, User user, LocalDateTime expiryDate) {
        super(token, user, expiryDate);
    }
}
