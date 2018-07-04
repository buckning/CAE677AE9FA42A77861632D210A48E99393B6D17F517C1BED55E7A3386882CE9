package com.amcglynn.priorityqueue.dal;

public interface QueueDao {
    void create(Long id, String date);
    boolean contains(Long id);
}
