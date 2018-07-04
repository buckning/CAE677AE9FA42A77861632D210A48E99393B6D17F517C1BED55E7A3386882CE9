package com.amcglynn.priorityqueue;

import com.amcglynn.priorityqueue.requests.WorkOrderRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class PriorityQueueController {
    @RequestMapping(value = "queue", method = POST)
    public WorkOrderRequest enqueue(@Valid @RequestBody WorkOrderRequest request) {
        return request;
    }
}
