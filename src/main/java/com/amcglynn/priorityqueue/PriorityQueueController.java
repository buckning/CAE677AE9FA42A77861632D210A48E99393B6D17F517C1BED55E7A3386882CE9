package com.amcglynn.priorityqueue;

import com.amcglynn.priorityqueue.requests.WorkOrderRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class PriorityQueueController {

    @RequestMapping(value = "helloWorld")
    public HelloWorldResponse helloWorld() {
        return new HelloWorldResponse("Hello world!");
    }

    @RequestMapping(value = "enqueue", method = POST)
    public WorkOrderRequest enqueue(@RequestBody WorkOrderRequest request) {
        return request;
    }
}
