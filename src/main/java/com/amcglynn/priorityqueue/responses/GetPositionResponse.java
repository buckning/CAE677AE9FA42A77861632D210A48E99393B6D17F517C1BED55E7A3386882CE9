package com.amcglynn.priorityqueue.responses;

public class GetPositionResponse {
    private Long position;

    public GetPositionResponse(Long position) {
        this.position = position;
    }

    public Long getPosition() {
        return position;
    }
}
