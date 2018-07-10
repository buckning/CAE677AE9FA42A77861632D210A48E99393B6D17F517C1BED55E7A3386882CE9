package com.amcglynn.priorityqueue.service;

import com.amcglynn.priorityqueue.dal.QueueEntry;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;

/***
 * This class is used to sort the queue based on rank and ID Class.
 */
public class PriorityQueueComparator implements Comparator<QueueEntry> {
    private DateProvider dateProvider;

    @Autowired
    public PriorityQueueComparator(DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }

    @Override
    public int compare(QueueEntry q1, QueueEntry q2) {
        if (q1.getClassIdType().equals(ClassIdType.MANAGEMENT_OVERRIDE)
                && q2.getClassIdType().getPriority() < q1.getClassIdType().getPriority()) {
            return -1;
        }

        if (q1.getClassIdType().equals(ClassIdType.MANAGEMENT_OVERRIDE)
                && q2.getClassIdType().equals(ClassIdType.MANAGEMENT_OVERRIDE)) {
            if (dateProvider.getTimeDifferenceInSeconds(q1.getDate(), q2.getDate()) < 0) {
                return -1;
            }
        }

        if (!q1.getClassIdType().equals(ClassIdType.MANAGEMENT_OVERRIDE)
                && !q2.getClassIdType().equals(ClassIdType.MANAGEMENT_OVERRIDE)) {
            if (q2.getRank() < q1.getRank()) {
                return -1;
            }
        }

        return 0;
    }
}
