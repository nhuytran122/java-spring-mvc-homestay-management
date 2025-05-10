package com.lullabyhomestay.homestay_management.domain.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RevenueBreakdownDTO {
    private Double roomRevenue;
    private Double serviceRevenue;
    private Double extensionRevenue;
}