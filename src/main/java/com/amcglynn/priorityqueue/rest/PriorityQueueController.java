package com.amcglynn.priorityqueue.rest;

import com.amcglynn.priorityqueue.requests.WorkOrderRequest;
import com.amcglynn.priorityqueue.service.PriorityQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class PriorityQueueController {
    @Autowired
    private PriorityQueueService priorityQueueService;

    @RequestMapping(value = "queue", method = POST)
    public WorkOrderRequest enqueue(@Valid @RequestBody WorkOrderRequest request) {
        priorityQueueService.createEntryInQueue(request.getUserId(), request.getDate());
        return request;
    }
}
