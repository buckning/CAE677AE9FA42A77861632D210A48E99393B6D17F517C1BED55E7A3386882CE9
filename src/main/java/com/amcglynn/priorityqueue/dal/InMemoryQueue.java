package com.amcglynn.priorityqueue.dal;

import java.util.HashMap;
import java.util.Map;

public class InMemoryQueue implements QueueDao {
    private Map<Long, String> queue;

    public InMemoryQueue() {
        this.queue = new HashMap<>();
    }

    @Override
    public void create(Long id, String date) {
        this.queue.put(id, date);
    }

    @Override
    public boolean contains(Long id) {
        return queue.get(id) != null;
    }

    public String getDate(Long id) {
        return queue.get(id);
    }
}
