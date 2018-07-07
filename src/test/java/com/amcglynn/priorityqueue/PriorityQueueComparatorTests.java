package com.amcglynn.priorityqueue;

import com.amcglynn.priorityqueue.dal.QueueEntry;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(DataProviderRunner.class)
public class PriorityQueueComparatorTests {

    @UseDataProvider("unorderedQueueEntryDataProvider")
    @Test
    public void testComparatorSortsUnorderedListsIntoTheCorrectOrder(QueueEntry qe1, QueueEntry qe2) {

        List<QueueEntry> list = new ArrayList<>();
        list.add(qe1);
        list.add(qe2);

        assertThat(list.get(0)).isEqualTo(qe1);
        assertThat(list.get(1)).isEqualTo(qe2);

        // using a real DateProvider implementation as it is very lightweight
        // and would be less work than configuring a mock
        Collections.sort(list, new PriorityQueueComparator(new DateProvider()));

        assertThat(list.get(0)).isEqualTo(qe2);
        assertThat(list.get(1)).isEqualTo(qe1);
    }

    @Test
    public void testLargeUnorderedListSort() {
        List<QueueEntry> list = new ArrayList<>();

        QueueEntry q1 = new QueueEntry(2L, "2018-01-01-00-00-02", ClassIdType.NORMAL, 2L);
        QueueEntry q2 = new QueueEntry(3L, "2018-01-01-00-00-03", ClassIdType.PRIORITY, 3L);
        QueueEntry q3 = new QueueEntry(5L, "2018-01-01-00-00-05", ClassIdType.VIP, 5L);
        QueueEntry q4 = new QueueEntry(30L, "2018-01-01-00-01-00", ClassIdType.MANAGEMENT_OVERRIDE, 100L);
        QueueEntry q5 = new QueueEntry(1L, "2018-01-01-00-00-01", ClassIdType.NORMAL, 1L);
        QueueEntry q6 = new QueueEntry(6L, "2018-01-01-00-00-04", ClassIdType.PRIORITY, 4L);
        QueueEntry q7 = new QueueEntry(15L, "2018-01-01-00-00-00", ClassIdType.MANAGEMENT_OVERRIDE, 1L);
        QueueEntry q8 = new QueueEntry(10L, "2018-01-01-00-00-10", ClassIdType.VIP, 10L);

        list.add(q1);
        list.add(q2);
        list.add(q3);
        list.add(q4);
        list.add(q5);
        list.add(q6);
        list.add(q7);
        list.add(q8);

        // using a real DateProvider implementation as it is very lightweight
        // and would be less work than configuring a mock
        Collections.sort(list, new PriorityQueueComparator(new DateProvider()));

        assertThat(list.get(0)).isEqualTo(q4);
        assertThat(list.get(1)).isEqualTo(q7);
        assertThat(list.get(2)).isEqualTo(q8);
        assertThat(list.get(3)).isEqualTo(q3);
        assertThat(list.get(4)).isEqualTo(q6);
        assertThat(list.get(5)).isEqualTo(q2);
        assertThat(list.get(6)).isEqualTo(q1);
        assertThat(list.get(7)).isEqualTo(q5);
    }

    @DataProvider
    public static Object[][] unorderedQueueEntryDataProvider() {
        return new Object[][] {
            {
                new QueueEntry(1L, "2018-01-01-00-00-00", ClassIdType.NORMAL, 1L),
                new QueueEntry(15L, "2018-01-01-00-00-00", ClassIdType.MANAGEMENT_OVERRIDE, 1L)
            },
            {
                new QueueEntry(15L, "2018-01-01-00-00-00", ClassIdType.MANAGEMENT_OVERRIDE, 1L),
                new QueueEntry(30L, "2018-01-01-00-00-10", ClassIdType.MANAGEMENT_OVERRIDE, 10L)
            },
            {
                new QueueEntry(1L, "2018-01-01-00-00-10", ClassIdType.NORMAL, 1L),
                new QueueEntry(2L, "2018-01-01-00-00-00", ClassIdType.NORMAL, 2L)
            },
            {
                new QueueEntry(1L, "2018-01-01-00-00-00", ClassIdType.NORMAL, 1L),
                new QueueEntry(5L, "2018-01-01-00-00-00", ClassIdType.VIP, 6L)
            },
            {
                new QueueEntry(3L, "2018-01-01-00-00-00", ClassIdType.PRIORITY, 5L),
                new QueueEntry(5L, "2018-01-01-00-00-00", ClassIdType.VIP, 6L)
            }
        };
    }
}
