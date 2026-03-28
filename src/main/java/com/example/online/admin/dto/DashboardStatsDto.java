package com.example.online.admin.dto;

public class DashboardStatsDto {
    private long totalBookings;
    private long todayEvents;
    private double monthlyRevenue;
    private long pendingActions;
    

    
    public long getTotalBookings() {
        return totalBookings;
    }
    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }
    public long getTodayEvents() {
        return todayEvents;
    }
    public void setTodayEvents(long todayEvents) {
        this.todayEvents = todayEvents;
    }
    public double getMonthlyRevenue() {
        return monthlyRevenue;
    }
    public void setMonthlyRevenue(double monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }
    public long getPendingActions() {
        return pendingActions;
    }
    public void setPendingActions(long pendingActions) {
        this.pendingActions = pendingActions;
    }

    
}
