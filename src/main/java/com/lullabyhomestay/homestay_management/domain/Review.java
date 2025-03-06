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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReviewID")
    private Long reviewID;

    @Column(name = "Rating")
    private int rating;

    @Column(name = "Comment")
    private String comment;

    @Column(name = "CreatedAt")
    private Date createdAt;

    @Column(name = "Image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "BookingID")
    private Booking booking;
}
