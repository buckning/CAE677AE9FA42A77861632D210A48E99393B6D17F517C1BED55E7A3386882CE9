package com.amcglynn.priorityqueue.dal;

public interface QueueDao {
    void create(Long id, String date);
    boolean contains(Long id);
    void delete(Long id);
    Long getUserPosition(Long id);
}
