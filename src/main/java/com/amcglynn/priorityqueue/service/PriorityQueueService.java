package com.amcglynn.priorityqueue.service;

import com.amcglynn.priorityqueue.dal.InMemoryQueue;
import com.amcglynn.priorityqueue.exceptions.ConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("priorityQueueService")
public class PriorityQueueService {
    private static final Logger LOG = LoggerFactory.getLogger(PriorityQueueService.class);

    @Autowired
    private InMemoryQueue inMemoryQueue;

    public void createEntryInQueue(Long id, String date) {
        if(inMemoryQueue.contains(id)) {
            LOG.info("Could not create entry in queue for ID: {} as entry already exists", id);
            throw new ConflictException();
        }
    }
}
