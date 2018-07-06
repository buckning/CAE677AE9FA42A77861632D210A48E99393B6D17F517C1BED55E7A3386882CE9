package com.amcglynn.priorityqueue.dal;

import com.amcglynn.priorityqueue.ClassIdType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryQueue implements QueueDao {
    private Map<Long, String> queue;
    private List<QueueEntry> queueEntryList;

    public InMemoryQueue() {
        this.queue = new HashMap<>();
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

    private class QueueEntry {
        public Long id;
        public String date;
        public ClassIdType classIdType;
        public Long rank;

        public QueueEntry(Long id, String date, ClassIdType classIdType, Long rank) {
            this.id = id;
            this.date = date;
            this.classIdType = classIdType;
            this.rank = rank;
        }

        public Long getId() {
            return id;
        }

        public String getDate() {
            return date;
        }

        public ClassIdType getClassIdType() {
            return classIdType;
        }

        public Long getRank() {
            return rank;
        }
    }
}
