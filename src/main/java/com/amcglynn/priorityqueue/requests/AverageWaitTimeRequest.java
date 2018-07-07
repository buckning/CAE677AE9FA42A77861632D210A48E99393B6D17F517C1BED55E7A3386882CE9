package com.amcglynn.priorityqueue.requests;

import com.amcglynn.priorityqueue.validation.annotations.Date;

public class AverageWaitTimeRequest {
    @Date
    private String fromTime;

    public AverageWaitTimeRequest() {
        this.fromTime = null;
    }

    public AverageWaitTimeRequest(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getFromTime() {
        return fromTime;
    }
}
