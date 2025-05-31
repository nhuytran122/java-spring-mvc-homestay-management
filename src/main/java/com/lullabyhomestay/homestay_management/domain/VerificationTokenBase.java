package com.lullabyhomestay.homestay_management.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class VerificationTokenBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long tokenId;

    @Column(name = "Token")
    protected String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    protected User user;

    protected LocalDateTime expiryDate;

    protected Boolean isUsed = false;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public VerificationTokenBase(String token, User user, LocalDateTime expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }
}