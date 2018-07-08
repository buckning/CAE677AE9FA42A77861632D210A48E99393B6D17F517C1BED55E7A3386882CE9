package com.amcglynn.priorityqueue.service;

import com.amcglynn.priorityqueue.ClassIdType;
import com.amcglynn.priorityqueue.DateProvider;
import com.amcglynn.priorityqueue.PriorityQueueComparator;
import com.amcglynn.priorityqueue.dal.InMemoryQueue;
import com.amcglynn.priorityqueue.dal.QueueEntry;
import com.amcglynn.priorityqueue.exceptions.ConflictException;
import com.amcglynn.priorityqueue.exceptions.NotFoundException;
import com.amcglynn.priorityqueue.responses.WorkOrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service("priorityQueueService")
public class PriorityQueueService {
    private static final Logger LOG = LoggerFactory.getLogger(PriorityQueueService.class);

    @Autowired
    private InMemoryQueue inMemoryQueue;

    @Autowired
    private DateProvider dateProvider;

    public PriorityQueueService(InMemoryQueue inMemoryQueue, DateProvider dateProvider) {
        this.inMemoryQueue = inMemoryQueue;
        this.dateProvider = dateProvider;
    }

    public WorkOrderResponse createEntryInQueue(Long id, String date) {
        if(inMemoryQueue.contains(id)) {
            LOG.info("Could not create entry in queue for ID: {} as entry already exists", id);
            throw new ConflictException();
        }

        ClassIdType classIdType = getClassId(id);
        Long rank = getRank(classIdType, date);
        inMemoryQueue.create(id, date, classIdType, rank);

        return new WorkOrderResponse(id, rank, date);
    }

    public List<WorkOrderResponse> getAllEntries() {
        List<QueueEntry> allEntries = inMemoryQueue.getAllEntries();
        allEntries.sort(new PriorityQueueComparator(dateProvider));
        return allEntries.stream()
                .map((queueEntry -> new WorkOrderResponse(queueEntry.getId(),
                        queueEntry.getRank(), queueEntry.getDate())))
                .collect(Collectors.toList());
    }

    public WorkOrderResponse getFromTopRequestFromQueue() {
        List<QueueEntry> allEntries = inMemoryQueue.getAllEntries();

        if (allEntries.isEmpty()) {
            throw new NotFoundException();
        }

        allEntries.sort(new PriorityQueueComparator(dateProvider));

        QueueEntry entry = allEntries.remove(0);
        inMemoryQueue.delete(entry.getId());
        return new WorkOrderResponse(entry.getId(), entry.getRank(), entry.getDate());
    }

    public Long getRank(ClassIdType classIdType, String date) {
        Long numberOfSecondsInQueue = dateProvider.getTimeDifferenceInSeconds(date, dateProvider.getCurrentTime());

        Long rank = -1L;

        if (classIdType == ClassIdType.NORMAL) {
            rank = numberOfSecondsInQueue;
        }
        return rank;
    }

    public Long getUserPositionFromQueue(Long id) {
        Long position = inMemoryQueue.getUserPosition(id);

        if (position == -1) {
            throw new NotFoundException();
        } else {
            return position;
        }
    }

    public Long getAverageWaitTime(String date) {
        List<QueueEntry> allEntries = inMemoryQueue.getAllEntries();
        if (allEntries.isEmpty()) {
            return 0L;
        }

        Long totalWaitTime = 0L;

        for (QueueEntry queueEntry: allEntries) {
            totalWaitTime += dateProvider.getTimeDifferenceInSeconds(queueEntry.getDate(), date);
        }

        return totalWaitTime / allEntries.size();
    }

    public Long get95thPercentileWaitTime(String date) {
        List<QueueEntry> allEntries = inMemoryQueue.getAllEntries();
        List<Long> waitTimes = allEntries.stream()
                .map((entry) -> dateProvider.getTimeDifferenceInSeconds(entry.getDate(), date))
                .collect(Collectors.toList());
        Collections.sort(waitTimes);

        int index = (int) Math.ceil(0.95 * waitTimes.size()) - 1;

        return waitTimes.get(index);
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

    public void removeFromQueue(Long userId) {
        inMemoryQueue.delete(userId);
    }
}
