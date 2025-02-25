package com.lullabyhomestay.homestay_management.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "RoomStatuses")
public class RoomStatus {
    @Id
    @Column(name = "StatusID")
    private int statusID;

    @Column(name = "StatusName")
    private int statusName;

    @OneToMany(mappedBy = "roomStatus")
    private List<RoomStatusHistory> roomStatusHistories;

}
