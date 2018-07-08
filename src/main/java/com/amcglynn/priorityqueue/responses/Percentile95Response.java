package com.amcglynn.priorityqueue.responses;

public class Percentile95Response {
    private Long percentile95;

    public Percentile95Response(Long percentile95) {
        this.percentile95 = percentile95;
    }

    public Long getPercentile95() {
        return percentile95;
    }
}
