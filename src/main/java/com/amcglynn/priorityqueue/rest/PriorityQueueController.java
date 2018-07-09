package com.amcglynn.priorityqueue.rest;

import com.amcglynn.priorityqueue.DateProvider;
import com.amcglynn.priorityqueue.exceptions.BadRequestException;
import com.amcglynn.priorityqueue.requests.WorkOrderRequest;
import com.amcglynn.priorityqueue.responses.AverageWaitTimeResponse;
import com.amcglynn.priorityqueue.responses.GetPositionResponse;
import com.amcglynn.priorityqueue.responses.Percentile95Response;
import com.amcglynn.priorityqueue.responses.WorkOrderResponse;
import com.amcglynn.priorityqueue.service.PriorityQueueService;
import com.amcglynn.priorityqueue.validation.validators.DateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.Validator;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class PriorityQueueController {
    @Autowired
    private PriorityQueueService priorityQueueService;

    @Autowired
    private DateValidator dateValidator;

    @RequestMapping(value = "queue", method = POST)
    public WorkOrderResponse enqueue(@Valid @RequestBody WorkOrderRequest request) {
        return priorityQueueService.createEntryInQueue(request.getUserId(), request.getDate());
    }

    @RequestMapping(value = "queue/{userId}", method = DELETE)
    public ResponseEntity deleteFromQueue(@PathVariable("userId") Long userId) {
        if (userId < 1) {
            throw new BadRequestException();
        }
        priorityQueueService.removeFromQueue(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "queue/{userId}", method = GET)
    public GetPositionResponse getIdFromQueue(@PathVariable("userId") Long userId) {
        if (userId < 1) {
            throw new BadRequestException();
        }
        return new GetPositionResponse(priorityQueueService.getUserPositionFromQueue(userId));
    }

    @RequestMapping(value = "queue/top", method = GET)
    public WorkOrderResponse getTopIdFromQueue() {
        return priorityQueueService.getFromTopRequestFromQueue();
    }

    @RequestMapping(value = "queue", method = GET)
    public List<WorkOrderResponse> getAllIdsFromQueue() {
        return priorityQueueService.getAllEntries();
    }

    @RequestMapping(value = "queue/avg-wait-time/{fromDate}", method = GET)
    public AverageWaitTimeResponse getAverageWaitTime(@PathVariable String fromDate) {
        if (!dateValidator.isValid(fromDate, null)) {
            throw new BadRequestException();
        }
        return new AverageWaitTimeResponse(priorityQueueService.getAverageWaitTime(fromDate));
    }

    @RequestMapping(value = "queue/percentile95", method = GET)
    public Percentile95Response get95thPercentile() {
        return new Percentile95Response(priorityQueueService
                .get95thPercentileWaitTime(new DateProvider().getCurrentTime()));
    }
}
