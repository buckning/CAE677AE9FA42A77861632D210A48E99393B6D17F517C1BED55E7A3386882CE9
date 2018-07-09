package com.amcglynn.priorityqueue.service;

public enum ClassIdType {
    NORMAL(0),
    PRIORITY(1),
    VIP(2),
    MANAGEMENT_OVERRIDE(3);

    private Integer priority;

    ClassIdType(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriority() {
        return priority;
    }
}
