package com.lullabyhomestay.homestay_management.domain;

import java.time.LocalDateTime;

import com.lullabyhomestay.homestay_management.utils.ActionType;
import com.lullabyhomestay.homestay_management.utils.ObjectType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ActionLogs")
public class ActionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LogID")
    private Long logID;

    @ManyToOne
    @JoinColumn(name = "employeeID")
    private Employee employee;

    @Column(name = "ActionType")
    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @Column(name = "ObjectType")
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

    @Column(name = "ObjectID")
    private Long objectID;

    @Column(name = "ActionTime", insertable = false)
    private LocalDateTime actionTime;
}
