package com.amcglynn.priorityqueue.dal;

import com.amcglynn.priorityqueue.ClassIdType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryQueueTest {
    private String date = "2018-01-01-00-00-00";
    private Long userId = 1L;

    @Test
    public void testCreateInsertsValueIntoQueue() {
        InMemoryQueue inMemoryQueue = new InMemoryQueue();
        inMemoryQueue.create(userId, date, ClassIdType.NORMAL, 10L);
        assertThat(inMemoryQueue.getDate(userId)).isEqualTo(date);
    }

    @Test
    public void testContainsReturnsFalseIfNotInQueue() {
        InMemoryQueue inMemoryQueue = new InMemoryQueue();
        assertThat(inMemoryQueue.contains(userId)).isFalse();
    }

    @Test
    public void testContainsReturnsTrueIfInQueue() {
        InMemoryQueue inMemoryQueue = new InMemoryQueue();
        inMemoryQueue.create(userId, date, ClassIdType.NORMAL, 10L);
        assertThat(inMemoryQueue.contains(userId)).isTrue();
    }

    @Test
    public void testDeleteFromQueue() {
        InMemoryQueue inMemoryQueue = new InMemoryQueue();
        inMemoryQueue.create(userId, date, ClassIdType.NORMAL, 10L);
        assertThat(inMemoryQueue.contains(userId)).isTrue();
        assertThat(inMemoryQueue.getDate(userId)).isEqualTo(date);
        inMemoryQueue.delete(userId);
        assertThat(inMemoryQueue.contains(userId)).isFalse();
    }

    @Test
    public void testGetUserPositionReturnsTheCorrectPosition() {
        InMemoryQueue inMemoryQueue = new InMemoryQueue();
        inMemoryQueue.create(1L, "2018-01-01-00-00-01", ClassIdType.NORMAL, 1L);
        inMemoryQueue.create(3L, "2018-01-01-00-00-03", ClassIdType.PRIORITY, 3L);

        assertThat(inMemoryQueue.getUserPosition(1L)).isEqualTo(0L);
        assertThat(inMemoryQueue.getUserPosition(3L)).isEqualTo(1L);
    }

    @Test
    public void testGetUserPositionReturnsMinusOneWhenUserNotFound() {
        InMemoryQueue inMemoryQueue = new InMemoryQueue();
        assertThat(inMemoryQueue.getUserPosition(1L)).isEqualTo(-1L);
    }
}
