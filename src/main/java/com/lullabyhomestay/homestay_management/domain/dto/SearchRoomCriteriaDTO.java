package com.lullabyhomestay.homestay_management.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SearchRoomCriteriaDTO {
    private Long roomTypeID;
    private Long branchID;

    public String convertToExtraParams() {
        StringBuilder extraParams = new StringBuilder();
        if (branchID != null) {
            extraParams.append("&branchID=").append(branchID);
        }
        if (roomTypeID != null) {
            extraParams.append("&roomTypeID=").append(roomTypeID);
        }
        return extraParams.toString();
    }
}
