package com.lullabyhomestay.homestay_management.domain.dto;

import java.time.LocalDateTime;

import com.lullabyhomestay.homestay_management.utils.SplitTimeRangeConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SearchRoomCriteriaDTO {
    private Long roomTypeID;
    private Long branchID;
    private String timeRange;

    public String convertToExtraParams() {
        StringBuilder extraParams = new StringBuilder();
        if (branchID != null) {
            extraParams.append("&branchID=").append(branchID);
        }
        if (roomTypeID != null) {
            extraParams.append("&roomTypeID=").append(roomTypeID);
        }
        if (timeRange != null && !timeRange.isEmpty()) {
            extraParams.append("&timeRange=").append(timeRange);
        }
        return extraParams.toString();
    }

    public LocalDateTime getFromTime() {
        return SplitTimeRangeConverter.parseStartDate(timeRange);
    }

    public LocalDateTime getToTime() {
        return SplitTimeRangeConverter.parseEndDate(timeRange);
    }
}
