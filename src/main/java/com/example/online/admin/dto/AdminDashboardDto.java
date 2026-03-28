package com.example.online.admin.dto;

import java.util.List;

public class AdminDashboardDto {
    private DashboardStatsDto stats;
    private List<BookingOverviewDto> bookingOverview;
    private BookingStatusDto bookingStatus;
    private List<RecentBookingDto> recentBookings;
    private List<UpcomingEventDto> upcomingEvents;
    private PendingActionsDto pendingActions;

    public DashboardStatsDto getStats() {
        return stats;
    }

    public void setStats(DashboardStatsDto stats) {
        this.stats = stats;
    }

    public List<BookingOverviewDto> getBookingOverview() {
        return bookingOverview;
    }

    public void setBookingOverview(List<BookingOverviewDto> bookingOverview) {
        this.bookingOverview = bookingOverview;
    }

    public BookingStatusDto getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatusDto bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public List<RecentBookingDto> getRecentBookings() {
        return recentBookings;
    }

    public void setRecentBookings(List<RecentBookingDto> recentBookings) {
        this.recentBookings = recentBookings;
    }

    public List<UpcomingEventDto> getUpcomingEvents() {
        return upcomingEvents;
    }

    public void setUpcomingEvents(List<UpcomingEventDto> upcomingEvents) {
        this.upcomingEvents = upcomingEvents;
    }

    public PendingActionsDto getPendingActions() {
        return pendingActions;
    }

    public void setPendingActions(PendingActionsDto pendingActions) {
        this.pendingActions = pendingActions;
    }
}
