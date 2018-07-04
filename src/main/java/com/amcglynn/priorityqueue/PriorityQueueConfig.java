package com.amcglynn.priorityqueue;

import com.amcglynn.priorityqueue.dal.InMemoryQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PriorityQueueConfig {

    @Bean
    public InMemoryQueue inMemoryQueue() {
        return new InMemoryQueue();
    }
}
