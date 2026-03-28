package com.example.online.vendor.dto;

import java.math.BigDecimal;
import java.util.List;

public class VendorEarningsResponseDto {

    private BigDecimal totalEarnings;
    private BigDecimal thisMonthEarnings;
    private BigDecimal pendingPayments;

    private List<EarningItemDto> items;

    public VendorEarningsResponseDto(
            BigDecimal totalEarnings,
            BigDecimal thisMonthEarnings,
            BigDecimal pendingPayments,
            List<EarningItemDto> items) {

        this.totalEarnings = totalEarnings;
        this.thisMonthEarnings = thisMonthEarnings;
        this.pendingPayments = pendingPayments;
        this.items = items;
    }

    public BigDecimal getTotalEarnings() { return totalEarnings; }
    public BigDecimal getThisMonthEarnings() { return thisMonthEarnings; }
    public BigDecimal getPendingPayments() { return pendingPayments; }
    public List<EarningItemDto> getItems() { return items; }
}