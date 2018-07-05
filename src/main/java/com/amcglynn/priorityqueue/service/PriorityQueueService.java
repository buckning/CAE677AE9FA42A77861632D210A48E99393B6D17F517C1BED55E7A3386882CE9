package com.amcglynn.priorityqueue.service;

import com.amcglynn.priorityqueue.ClassIdType;
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

    public ClassIdType getIdClass(Long id) {
        ClassIdType result;
        boolean priorityId = id % 3 == 0;
        boolean vipId = id % 5 == 0;
        boolean managementOverrideId = priorityId && vipId;

        if (managementOverrideId) {
            result = ClassIdType.MANAGEMENT_OVERRIDE;
        } else if (priorityId) {
            result = ClassIdType.PRIORITY;
        } else if (vipId) {
            result = ClassIdType.VIP;
        } else {
            result = ClassIdType.NORMAL;
        }

        return result;
    }
}
