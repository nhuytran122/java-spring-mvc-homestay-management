package com.lullabyhomestay.homestay_management.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SearchRoomCriteriaDTO {
    private Long roomTypeId;
    private Long branchId;

    public String convertToExtraParams() {
        StringBuilder extraParams = new StringBuilder();
        if (branchId != null) {
            extraParams.append("&branchId=").append(branchId);
        }
        if (roomTypeId != null) {
            extraParams.append("&roomTypeId=").append(roomTypeId);
        }
        return extraParams.toString();
    }
}
