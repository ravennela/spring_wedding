package com.example.online.admin.dto;

public class PendingActionsDto {
    private long vendorAssignmentCount;
    private long paymentReviewCount;

    public long getVendorAssignmentCount() {
        return vendorAssignmentCount;
    }
    public void setVendorAssignmentCount(long vendorAssignmentCount) {
        this.vendorAssignmentCount = vendorAssignmentCount;
    }
    public long getPaymentReviewCount() {
        return paymentReviewCount;
    }
    public void setPaymentReviewCount(long paymentReviewCount) {
        this.paymentReviewCount = paymentReviewCount;
    }

}
