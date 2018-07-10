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

    public static ClassIdType fromValue(Long id) {
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
}
