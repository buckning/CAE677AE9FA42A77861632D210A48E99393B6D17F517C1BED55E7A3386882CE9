package com.amcglynn.priorityqueue.responses;

import java.util.List;

/**
 * Created by amcglynn on 10/07/2018.
 */
public class GetAllIdsResponse {
    private List<WorkOrderResponse> allIds;

    public GetAllIdsResponse(List<WorkOrderResponse> allIds) {
        this.allIds = allIds;
    }

    public List<WorkOrderResponse> getAllIds() {
        return allIds;
    }
}
