package com.amcglynn.priorityqueue.responses;

public class WorkOrderResponse {

    private Long userId;
    private String date;

    public WorkOrderResponse(Long userId, String date) {
        this.userId = userId;
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }
}
