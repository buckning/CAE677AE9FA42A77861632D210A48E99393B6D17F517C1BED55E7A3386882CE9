package com.amcglynn.priorityqueue.dal;

import com.amcglynn.priorityqueue.service.ClassIdType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QueueEntry {
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

    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            return super.toString();
        }
    }
}
