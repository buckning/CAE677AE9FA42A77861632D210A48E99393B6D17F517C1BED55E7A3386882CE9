package com.amcglynn.priorityqueue.service;

import com.amcglynn.priorityqueue.ClassIdType;
import com.amcglynn.priorityqueue.DateProvider;
import com.amcglynn.priorityqueue.dal.InMemoryQueue;
import com.amcglynn.priorityqueue.exceptions.ConflictException;
import com.amcglynn.priorityqueue.responses.WorkOrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("priorityQueueService")
public class PriorityQueueService {
    private static final Logger LOG = LoggerFactory.getLogger(PriorityQueueService.class);

    @Autowired
    private InMemoryQueue inMemoryQueue;

    @Autowired
    private DateProvider dateProvider;

    public WorkOrderResponse createEntryInQueue(Long id, String date) {
        if(inMemoryQueue.contains(id)) {
            LOG.info("Could not create entry in queue for ID: {} as entry already exists", id);
            throw new ConflictException();
        }

        inMemoryQueue.create(id, date);

        return new WorkOrderResponse(id, getRank(getClassId(id), date), date);
    }

    public Long getRank(ClassIdType classIdType, String date) {
        Long numberOfSecondsInQueue = dateProvider.getTimeDifferenceInSeconds(date, dateProvider.getCurrentTime());

        Long rank = -1L;

        if (classIdType == ClassIdType.NORMAL) {
            rank = numberOfSecondsInQueue;
        }
        return rank;
    }

    public ClassIdType getClassId(Long id) {
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
