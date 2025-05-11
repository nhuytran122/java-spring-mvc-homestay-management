package com.lullabyhomestay.homestay_management.domain.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class ReportResultDTO {
    private List<String> labels;
    private List<BigDecimal> revenues;
    private BigDecimal totalRevenue;
}