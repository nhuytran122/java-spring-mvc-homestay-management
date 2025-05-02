package com.lullabyhomestay.homestay_management.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class Person {
    @Column(name = "FullName")
    protected String fullName;

    @Column(name = "Phone")
    protected String phone;

    @Column(name = "Email")
    protected String email;

    @Column(name = "Address")
    protected String address;

    @Column(name = "Avatar")
    protected String avatar;

    @Column(name = "Password")
    private String password;
}
