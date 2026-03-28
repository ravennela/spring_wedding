package com.example.online.admin.dto;

import com.example.online.common.enums.BookingStatus;

public class UpcomingEventDto {
    private String title;
    private String date;
    private String time;
    private String vendorName;
    private BookingStatus  status;

    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getVendorName() {
        return vendorName;
    }
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
    public BookingStatus  getStatus() {
        return status;
    }
    public void setStatus(BookingStatus  status) {
        this.status = status;
    }
    
}
