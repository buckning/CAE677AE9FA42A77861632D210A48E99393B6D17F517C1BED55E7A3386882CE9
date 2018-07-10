package com.amcglynn.priorityqueue.rest;

import com.amcglynn.priorityqueue.service.DateProvider;
import com.amcglynn.priorityqueue.exceptions.BadRequestException;
import com.amcglynn.priorityqueue.requests.WorkOrderRequest;
import com.amcglynn.priorityqueue.responses.AverageWaitTimeResponse;
import com.amcglynn.priorityqueue.responses.GetAllIdsResponse;
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

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class PriorityQueueController {
    @Autowired
    private PriorityQueueService priorityQueueService;

    @Autowired
    private DateValidator dateValidator;

    /***
     * Add to the queue
     * @param request request body
     * @return response
     */
    @RequestMapping(value = "queue", method = POST)
    public WorkOrderResponse enqueue(@Valid @RequestBody WorkOrderRequest request) {
        return priorityQueueService.createEntryInQueue(request.getUserId(), request.getDate());
    }

    /***
     * Delete a specific entry from the queue
     * @param userId id to be removed
     * @return 204 - No Content
     */
    @RequestMapping(value = "queue/{userId}", method = DELETE)
    public ResponseEntity deleteFromQueue(@PathVariable("userId") Long userId) {
        if (userId < 1) {
            throw new BadRequestException();
        }
        priorityQueueService.removeFromQueue(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /***
     * Get a specific entry from the queue
     * @param userId ID being looked up
     * @return response
     */
    @RequestMapping(value = "queue/{userId}", method = GET)
    public GetPositionResponse getIdFromQueue(@PathVariable("userId") Long userId) {
        if (userId < 1) {
            throw new BadRequestException();
        }
        return new GetPositionResponse(priorityQueueService.getUserPositionFromQueue(userId));
    }

    /***
     * Get the entry that is on the top of the queue
     * @return the entry on top of the queue
     */
    @RequestMapping(value = "queue/top", method = GET)
    public WorkOrderResponse getTopIdFromQueue() {
        return priorityQueueService.getFromTopRequestFromQueue();
    }

    /***
     * Get all entries in the queue
     * @return list of entries in the queue
     */
    @RequestMapping(value = "queue", method = GET)
    public GetAllIdsResponse getAllIdsFromQueue() {
        return new GetAllIdsResponse(priorityQueueService.getAllEntries());
    }

    /***
     * Get the average wait time for requests in the queue
     * @param fromDate date from which the average wait time will be based off
     * @return The average wait time
     */
    @RequestMapping(value = "queue/avg-wait-time/{fromDate}", method = GET)
    public AverageWaitTimeResponse getAverageWaitTime(@PathVariable String fromDate) {
        if (!dateValidator.isValid(fromDate, null)) {
            throw new BadRequestException();
        }
        return new AverageWaitTimeResponse(priorityQueueService.getAverageWaitTime(fromDate));
    }

    /***
     * Get the 95th percentile of the wait time from the queue
     * @return 95th percentile wait time
     */
    @RequestMapping(value = "queue/percentile95", method = GET)
    public Percentile95Response get95thPercentile() {
        return new Percentile95Response(priorityQueueService
                .get95thPercentileWaitTime(new DateProvider().getCurrentTime()));
    }
}
