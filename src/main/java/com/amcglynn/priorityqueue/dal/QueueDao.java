package com.amcglynn.priorityqueue.dal;

import com.amcglynn.priorityqueue.service.ClassIdType;

import java.util.Optional;

public interface QueueDao {
    void create(Long id, String date, ClassIdType classIdType, Long rank);
    boolean contains(Long id);
    void delete(Long id);
    Long getUserPosition(Long id);
    void addWaitTimeForCompletedTask(Long completedTaskWaitTime);
    Optional<QueueEntry> getEntry(Long id);
}
