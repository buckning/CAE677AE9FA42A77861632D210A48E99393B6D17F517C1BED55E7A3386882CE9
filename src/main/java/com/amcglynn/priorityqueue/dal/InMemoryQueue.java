package com.amcglynn.priorityqueue.dal;

import com.amcglynn.priorityqueue.ClassIdType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryQueue implements QueueDao {
    private List<QueueEntry> queueEntryList;

    public InMemoryQueue() {
        this.queueEntryList = new ArrayList<>();
    }

    @Override
    public void create(Long id, String date, ClassIdType classIdType, Long rank) {
        queueEntryList.add(new QueueEntry(id, date, classIdType, rank));
    }

    @Override
    public boolean contains(Long id) {
        return queueEntryList.stream().filter((entry) -> entry.getId() == id).findFirst().isPresent();
    }

    @Override
    public void delete(Long id) {
        queueEntryList.removeIf((entry) -> entry.getId() == id);
    }

    @Override
    public Long getUserPosition(Long id) {
        return 100L;
    }

    public String getDate(Long id) {
        return queueEntryList.stream().filter((entry) -> entry.getId() == id).findFirst().get().getDate();
    }
}
