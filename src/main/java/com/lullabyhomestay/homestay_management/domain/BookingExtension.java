package com.lullabyhomestay.homestay_management.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "BookingExtensions")
public class BookingExtension {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ExtensionID")
    private Long extensionID;

    @Column(name = "ExtraHours")
    @NotBlank(message = "Vui lòng nhập giờ muốn gia hạn")
    private int extraHours;

    @Column(name = "CreatedAt")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "BookingID")
    private Booking booking;
}
