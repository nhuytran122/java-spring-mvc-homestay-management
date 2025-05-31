package com.lullabyhomestay.homestay_management.domain.dto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchTransactionCriterialDTO {
    private String keyword;
    private Long branchId;
    private String transactionType;
    private String sort;

    public String convertToExtraParams() {
        StringBuilder extraParams = new StringBuilder();
        if (branchId != null) {
            extraParams.append("&branchId=").append(branchId);
        }
        if (transactionType != null && !transactionType.isEmpty()) {
            extraParams.append("&transactionType=").append(transactionType);
        }
        if (sort != null && !sort.isEmpty()) {
            extraParams.append("&sort=").append(sort);
        }
        if (keyword != null && !keyword.isEmpty()) {
            extraParams.append("&keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8));
        }
        return extraParams.toString();
    }
}