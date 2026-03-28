package com.example.online.admin.dto;

public class BookingStatusDto {
    private long confirmed;
    private long pending;
    private long cancelled;
    public long getConfirmed() {
        return confirmed;
    }
    public void setConfirmed(long confirmed) {
        this.confirmed = confirmed;
    }
    public long getPending() {
        return pending;
    }
    public void setPending(long pending) {
        this.pending = pending;
    }
    public long getCancelled() {
        return cancelled;
    }
    public void setCancelled(long cancelled) {
        this.cancelled = cancelled;
    }
    
}
