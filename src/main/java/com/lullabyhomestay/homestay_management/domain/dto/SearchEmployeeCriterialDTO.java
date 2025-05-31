package com.lullabyhomestay.homestay_management.domain.dto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SearchEmployeeCriterialDTO {
    private String keyword;
    private Long roleId;

    public String convertToExtraParams() {
        StringBuilder extraParams = new StringBuilder();
        if (roleId != null) {
            extraParams.append("&roleId=").append(roleId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            extraParams.append("&keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8));
        }
        return extraParams.toString();
    }
}
