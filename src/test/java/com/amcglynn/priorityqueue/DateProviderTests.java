package com.amcglynn.priorityqueue;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DateProviderTests {
    @Test
    public void testGetCurrentTimeReturnsAValidTime() {
        String currentTime = new DateProvider().getCurrentTime();
        assertThat(currentTime.matches("\\d{4}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}")).isTrue();
    }

    @Test
    public void testGetTimeDifferenceInSecondsReturnsTheCorrectValue() {
        assertThat(new DateProvider().getTimeDifferenceInSeconds("2018-07-05-00-00-00", "2018-07-05-00-00-10")).isEqualTo(10L);
    }

    @Test
    public void testGetTimeDifferenceInSecondsReturnsErrorCodeWhenCouldNotParseText() {
        assertThat(new DateProvider().getTimeDifferenceInSeconds("2018-07-05-00-0000", "2018-07-05-00-00-10")).isEqualTo(-1);
    }
}
