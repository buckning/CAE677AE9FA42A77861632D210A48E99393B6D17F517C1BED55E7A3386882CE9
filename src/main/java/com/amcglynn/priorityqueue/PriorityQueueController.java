package com.amcglynn.priorityqueue;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PriorityQueueController {

    @RequestMapping(value = "helloWorld")
    public HelloWorldResponse helloWorld() {
        return new HelloWorldResponse("Hello world!");
    }
}
