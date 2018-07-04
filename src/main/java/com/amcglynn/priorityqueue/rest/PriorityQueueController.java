package com.amcglynn.priorityqueue.rest;

import com.amcglynn.priorityqueue.exceptions.BadRequestException;
import com.amcglynn.priorityqueue.requests.WorkOrderRequest;
import com.amcglynn.priorityqueue.service.PriorityQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
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

    @RequestMapping(value = "queue/{userId}", method = DELETE)
    public ResponseEntity deleteFromQueue(@PathVariable("userId") Long userId) {
        if (userId < 1) {
            throw new BadRequestException();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
