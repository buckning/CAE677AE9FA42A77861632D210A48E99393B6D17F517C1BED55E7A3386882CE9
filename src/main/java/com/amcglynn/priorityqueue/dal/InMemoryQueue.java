package com.amcglynn.priorityqueue.dal;

import com.amcglynn.priorityqueue.service.ClassIdType;
import com.amcglynn.priorityqueue.service.DateProvider;
import com.amcglynn.priorityqueue.service.PriorityQueueComparator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryQueue implements QueueDao {
    private List<QueueEntry> queueEntryList;
    private List<Long> completedTasksWaitTime;

    @Autowired
    private DateProvider dateProvider;

    public InMemoryQueue() {
        this.queueEntryList = new ArrayList<>();
        this.completedTasksWaitTime = new ArrayList<>();
    }

    @Override
    public void addWaitTimeForCompletedTask(Long completedTaskWaitTime) {
        completedTasksWaitTime.add(completedTaskWaitTime);
    }

    @Override
    public void create(Long id, String date, ClassIdType classIdType, Long rank) {
        queueEntryList.add(new QueueEntry(id, date, classIdType, rank));
    }

    @Override
    public Optional<QueueEntry> getEntry(Long id) {
        return queueEntryList.stream().filter((entry) -> entry.getId() == id).findFirst();
    }

    @Override
    public boolean contains(Long id) {
        return getEntry(id).isPresent();
    }

    @Override
    public void delete(Long id) {
        queueEntryList.removeIf((entry) -> entry.getId() == id);
    }

    @Override
    public Long getUserPosition(Long id) {
        Long position;
        queueEntryList.sort(new PriorityQueueComparator(dateProvider));
        Optional<QueueEntry> queueEntry = queueEntryList.stream().filter((entry) -> entry.getId() == id).findFirst();
        if (queueEntry.isPresent()) {
            position = Long.valueOf(queueEntryList.indexOf(queueEntry.get()));
        } else {
            position = -1L;
        }

        return position;
    }

    @Override
    public List<QueueEntry> getAllEntries() {
        return new ArrayList<>(queueEntryList);
    }

    public String getDate(Long id) {
        return queueEntryList.stream().filter((entry) -> entry.getId() == id).findFirst().get().getDate();
    }

    @Override
    public List<Long> getWaitTimesForCompletedTasks() {
        return completedTasksWaitTime;
    }
}
