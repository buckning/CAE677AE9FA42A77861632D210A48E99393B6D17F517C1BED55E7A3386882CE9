package com.amcglynn.priorityqueue.service;

import com.amcglynn.priorityqueue.dal.QueueDao;
import com.amcglynn.priorityqueue.dal.QueueEntry;
import com.amcglynn.priorityqueue.exceptions.ConflictException;
import com.amcglynn.priorityqueue.exceptions.NotFoundException;
import com.amcglynn.priorityqueue.responses.WorkOrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("priorityQueueService")
public class PriorityQueueService {
    private static final Logger LOG = LoggerFactory.getLogger(PriorityQueueService.class);

    @Autowired
    private QueueDao queueDao;

    @Autowired
    private DateProvider dateProvider;

    public PriorityQueueService(QueueDao queueDao, DateProvider dateProvider) {
        this.queueDao = queueDao;
        this.dateProvider = dateProvider;
    }

    /***
     * Create an entry in the queue. Throws 409 exception if the ID already has an entry in the queue.
     * @param id ID being created
     * @param date date being created
     * @return id and date if successful
     */
    public WorkOrderResponse createEntryInQueue(Long id, String date) {
        if(queueDao.contains(id)) {
            LOG.info("Could not create entry in queue for ID: {} as entry already exists", id);
            throw new ConflictException();
        }

        ClassIdType classIdType = ClassIdType.fromValue(id);
        Long rank = getRank(classIdType, date);
        queueDao.create(id, date, classIdType, rank);

        return new WorkOrderResponse(id, date);
    }

    /***
     * Get a list of all entries in the queue.
     * @return all entries in the queue.
     */
    public List<WorkOrderResponse> getAllEntries() {
        List<QueueEntry> allEntries = queueDao.getAllEntries();
        allEntries.sort(new PriorityQueueComparator(dateProvider));
        return allEntries.stream()
                .map((queueEntry -> new WorkOrderResponse(queueEntry.getId(), queueEntry.getDate())))
                .collect(Collectors.toList());
    }

    /***
     * Get the entry from the top of the queue. Once it is read off the queue, it is removed.
     * @return The top entry from the queue
     */
    public WorkOrderResponse getFromTopRequestFromQueue() {
        List<QueueEntry> allEntries = queueDao.getAllEntries();

        if (allEntries.isEmpty()) {
            throw new NotFoundException();
        }

        allEntries.sort(new PriorityQueueComparator(dateProvider));

        QueueEntry entry = allEntries.remove(0);
        queueDao.delete(entry.getId());
        queueDao.addWaitTimeForCompletedTask(dateProvider
                .getTimeDifferenceInSeconds(entry.getDate(), dateProvider.getCurrentTime()));
        return new WorkOrderResponse(entry.getId(), entry.getDate());
    }

    /***
     * Get the rank of a queue entry.
     * Normal IDs are given a rank equal to the number of seconds they’ve been in the queue.
     *
     * Priority IDs are given a rank equal to the result of applying the following formula to the number of
     * seconds they’ve been in the queue:
     * max(3,nlogn)
     *
     * VIP IDs are given a rank equal to the result of applying the following  formula to the number of
     * seconds they’ve been in the queue:
     * max(4, 2n log n)
     *
     * Management Override IDs are always ranked ahead of all other IDs and are ranked among themselves according to the
     * number of seconds they’ve been in the queue.
     * @param classIdType
     * @param date
     * @return
     */
    public Long getRank(ClassIdType classIdType, String date) {
        Long numberOfSecondsInQueue = dateProvider.getTimeDifferenceInSeconds(date, dateProvider.getCurrentTime());

        Long rank;

        if (classIdType == ClassIdType.PRIORITY) {
            rank = (long) Math.max(3, numberOfSecondsInQueue * Math.log(numberOfSecondsInQueue));
        } else if (classIdType == ClassIdType.VIP) {
            rank = (long) Math.max(4, 2 * numberOfSecondsInQueue * Math.log(numberOfSecondsInQueue));
        } else {
            rank = numberOfSecondsInQueue;
        }
        return rank;
    }

    /***
     * Get the position of an entry in the queue. Throws 404 exception if entry not found.
     * Positions start at 0
     * @param id id being looked up
     * @return position of the ID
     */
    public Long getUserPositionFromQueue(Long id) {
        Long position = queueDao.getUserPosition(id);

        if (position == -1) {
            throw new NotFoundException();
        } else {
            return position;
        }
    }

    /***
     * Get the average wait time for entry in the queue. Only processes entries that are waiting, future entries are
     * discarded from the average wait time.
     * @param date time to compare the queue entries with
     * @return average wait time
     */
    public Long getAverageWaitTime(String date) {
        List<QueueEntry> allEntries = queueDao.getAllEntries();
        if (allEntries.isEmpty()) {
            return 0L;
        }

        Long totalWaitTime = 0L;

        for (QueueEntry queueEntry: allEntries) {
            long timeDifferenceForEntry = dateProvider.getTimeDifferenceInSeconds(queueEntry.getDate(), date);

            if (timeDifferenceForEntry > 0) {
                totalWaitTime += dateProvider.getTimeDifferenceInSeconds(queueEntry.getDate(), date);
            } else {
                LOG.info("ignoring time difference for ID: {} as it was not in the queue at the requested time",
                        queueEntry.getId(), queueEntry.getDate());
            }
        }

        return totalWaitTime / allEntries.size();
    }

    /***
     * Get the 95th percentile wait time for current jobs in the queue and completed or removed jobs
     * @param date used to get the wait time per entry
     * @return 95th percentile wait time
     */
    public Long get95thPercentileWaitTime(String date) {
        List<QueueEntry> allEntries = queueDao.getAllEntries();
        List<Long> waitTimes = allEntries.stream()
                .map((entry) -> dateProvider.getTimeDifferenceInSeconds(entry.getDate(), date))
                .collect(Collectors.toList());
        waitTimes.addAll(queueDao.getWaitTimesForCompletedTasks());
        Collections.sort(waitTimes);

        int index = (int) Math.ceil(0.95 * waitTimes.size()) - 1;

        return waitTimes.get(index);
    }

    /***
     * Remove an item from the queue if it exists. Once it is removed, the wait time is saved in a separate list
     * for processing the 95th percentile later
     * @param userId id of the entry getting deleted
     */
    public void removeFromQueue(Long userId) {
        Optional<QueueEntry> queueEntry = queueDao.getEntry(userId);
        if (queueEntry.isPresent()) {
            QueueEntry entry = queueEntry.get();
            queueDao.delete(userId);
            queueDao.addWaitTimeForCompletedTask(dateProvider
                    .getTimeDifferenceInSeconds(entry.getDate(), dateProvider.getCurrentTime()));
        }
    }
}
