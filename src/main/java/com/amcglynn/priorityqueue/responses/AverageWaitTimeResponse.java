package com.amcglynn.priorityqueue.responses;

public class AverageWaitTimeResponse {
    private Long averageWaitTime;

    public AverageWaitTimeResponse(Long averageWaitTime) {
        this.averageWaitTime = averageWaitTime;
    }

    public Long getAverageWaitTime() {
        return averageWaitTime;
    }
}
