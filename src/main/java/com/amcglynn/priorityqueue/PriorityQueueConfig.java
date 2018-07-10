package com.amcglynn.priorityqueue;

import com.amcglynn.priorityqueue.dal.InMemoryQueue;
import com.amcglynn.priorityqueue.dal.QueueDao;
import com.amcglynn.priorityqueue.service.DateProvider;
import com.amcglynn.priorityqueue.validation.validators.DateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PriorityQueueConfig {

    @Bean
    public QueueDao queueDao() {
        return new InMemoryQueue();
    }

    @Bean
    public DateValidator validator() {
        return new DateValidator();
    }

    @Bean
    public DateProvider dateProvider() {
        return new DateProvider();
    }
}
