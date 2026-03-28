package com.example.online.vendor.dto;

import java.util.List;

public class VendorDashboardResponseDto {

    private int pending;
    private int accepted;
    private int completed;
    private Double totalEarnings;

    private List<RecentJobDto> recentJobs;

    public VendorDashboardResponseDto(
            int pending,
            int accepted,
            int completed,
            Double totalEarnings,
            List<RecentJobDto> recentJobs) {

        this.pending = pending;
        this.accepted = accepted;
        this.completed = completed;
        this.totalEarnings = totalEarnings;
        this.recentJobs = recentJobs;
    }

    public int getPending() { return pending; }
    public int getAccepted() { return accepted; }
    public int getCompleted() { return completed; }
    public Double getTotalEarnings() { return totalEarnings; }
    public List<RecentJobDto> getRecentJobs() { return recentJobs; }
}