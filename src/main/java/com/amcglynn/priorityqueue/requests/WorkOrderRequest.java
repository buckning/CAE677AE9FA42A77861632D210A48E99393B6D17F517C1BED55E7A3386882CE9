package com.amcglynn.priorityqueue.requests;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class WorkOrderRequest {

    @NotNull
    @Min(1)
    @Max(Long.MAX_VALUE)
    private Long userId;

    @NotNull
    private String date;

    public WorkOrderRequest() {
        this.userId = 0L;
        this.date = "invalid";
    }

    public WorkOrderRequest(Long userId, String date) {
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
