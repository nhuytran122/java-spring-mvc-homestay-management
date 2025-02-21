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
@Table(name = "MaintainanceRequests")
public class MaintainanceRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RequestID")
    private long requestID;

    @Column(name = "Description")
    private String description;
    
    @Column(name = "Status")
    private String status;
    
    @Column(name = "CreatedAt")
    private Date createdAt;
    
    @Column(name = "UpdatedAt")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "RoomID")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "EmployeeID")
    private Employee employee;
}
