package com.amcglynn.priorityqueue.responses;

public class WorkOrderResponse {

    private Long userId;
    private Long rank;
    private String date;

    public WorkOrderResponse(Long userId, Long rank, String date) {
        this.userId = userId;
        this.date = date;
        this.rank = rank;
    }

    public Long getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public Long getRank() {
        return rank;
    }
}
