package com.lullabyhomestay.homestay_management.domain.dto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SearchBookingServiceCriteriaDTO {
    private String keyword;
    private Boolean isPrepaid;
    private String sort;

    public String convertToExtraParams() {
        StringBuilder extraParams = new StringBuilder();
        if (isPrepaid != null) {
            extraParams.append("&isPrepaid=").append(isPrepaid);
        }
        if (keyword != null && !keyword.isEmpty()) {
            extraParams.append("&keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8));
        }
        if (sort != null) {
            extraParams.append("&sort=").append(sort);
        }
        return extraParams.toString();
    }
}