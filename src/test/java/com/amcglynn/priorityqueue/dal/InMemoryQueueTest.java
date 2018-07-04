package com.amcglynn.priorityqueue.dal;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryQueueTest {
    @Test
    public void testCreateInsertsValueIntoQueue() {
        InMemoryQueue inMemoryQueue = new InMemoryQueue();
        inMemoryQueue.create(1L, "01012018");
        assertThat(inMemoryQueue.getDate(1L)).isEqualTo("01012018");
    }

    @Test
    public void testContainsReturnsFalseIfNotInQueue() {
        InMemoryQueue inMemoryQueue = new InMemoryQueue();
        assertThat(inMemoryQueue.contains(1L)).isFalse();
    }

    @Test
    public void testContainsReturnsTrueIfInQueue() {
        InMemoryQueue inMemoryQueue = new InMemoryQueue();
        inMemoryQueue.create(1L, "01012018");
        assertThat(inMemoryQueue.contains(1L)).isTrue();
    }
}
