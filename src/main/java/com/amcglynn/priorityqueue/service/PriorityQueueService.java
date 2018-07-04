package com.amcglynn.priorityqueue.service;

import com.amcglynn.priorityqueue.dal.InMemoryQueue;
import com.amcglynn.priorityqueue.exceptions.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("priorityQueueService")
public class PriorityQueueService {

    @Autowired
    private InMemoryQueue inMemoryQueue;

    public void createEntryInQueue(Long id, String date) {
        if(inMemoryQueue.contains(id)) {
            throw new ConflictException();
        }
    }
}
