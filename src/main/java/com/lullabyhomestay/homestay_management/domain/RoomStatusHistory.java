package com.lullabyhomestay.homestay_management.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class RoomStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoomStatusID")
    private Long roomStatusID;

    @Column(name = "StartedAt")
    private Date startedAt;

    @Column(name = "EndedAt")
    private Date endedAt;

    @ManyToOne
    @JoinColumn(name = "roomID")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "roomStatusID")
    private RoomStatus roomStatus;

    @ManyToOne
    @JoinColumn(name = "bookingID")
    private Booking booking;
}
