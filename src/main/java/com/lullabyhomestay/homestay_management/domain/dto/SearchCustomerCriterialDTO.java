package com.lullabyhomestay.homestay_management.domain.dto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SearchCustomerCriterialDTO {
    private String keyword;
    private Long customerTypeId;
    private String sort;

    public String convertToExtraParams() {
        StringBuilder extraParams = new StringBuilder();
        if (customerTypeId != null) {
            extraParams.append("&customerTypeId=").append(customerTypeId);
        }
        if (sort != null) {
            extraParams.append("&sort=").append(sort);
        }
        if (keyword != null && !keyword.isEmpty()) {
            extraParams.append("&keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8));
        }
        return extraParams.toString();
    }
}
