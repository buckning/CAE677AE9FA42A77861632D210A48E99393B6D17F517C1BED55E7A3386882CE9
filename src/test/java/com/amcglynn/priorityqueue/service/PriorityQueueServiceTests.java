package com.amcglynn.priorityqueue.service;

import com.amcglynn.priorityqueue.dal.InMemoryQueue;
import com.amcglynn.priorityqueue.dal.QueueEntry;
import com.amcglynn.priorityqueue.exceptions.ConflictException;
import com.amcglynn.priorityqueue.exceptions.NotFoundException;
import com.amcglynn.priorityqueue.responses.WorkOrderResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PriorityQueueServiceTests {

    @Mock
    private InMemoryQueue inMemoryQueueMock;

    @Mock
    private DateProvider dateProviderMock;

    @InjectMocks
    private PriorityQueueService service;

    @Test
    public void testCreateEntryInQueueThrows409ConflictExceptionWhenIdIsAlreadyInQueue() {
        when(inMemoryQueueMock.contains(1L)).thenReturn(true);
        Throwable throwable = catchThrowable(() -> service.createEntryInQueue(1L, "2018-01-01-00-00-10"));
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(ConflictException.class);
    }

    @Test
    public void testCreateEntryInQueueCompletesSuccessfully() {
        Long userId = 1L;
        String date = "2018-01-01-00-00-00";
        when(dateProviderMock.getCurrentTime()).thenReturn("2018-01-01-00-00-10");
        when(dateProviderMock.getTimeDifferenceInSeconds(anyString(), anyString())).thenReturn(Long.valueOf(10L));

        WorkOrderResponse response = service.createEntryInQueue(userId, date);

        verify(inMemoryQueueMock, times(1)).create(1L, date, ClassIdType.NORMAL, 10L);
        assertThat(response.getDate()).isEqualTo(date);
        assertThat(response.getUserId()).isEqualTo(1L);
    }

    @Test
    public void testGetRankForNormalId() {
        when(dateProviderMock.getCurrentTime()).thenReturn("2018-01-01-00-00-10");
        when(dateProviderMock.getTimeDifferenceInSeconds(anyString(), anyString())).thenReturn(Long.valueOf(10L));
        assertThat(service.getRank(ClassIdType.NORMAL, "2018-01-01-00-00-00")).isEqualTo(10L);
    }

    @Test
    public void testGetRankForManagementOverrideId() {
        when(dateProviderMock.getCurrentTime()).thenReturn("2018-01-01-00-00-10");
        when(dateProviderMock.getTimeDifferenceInSeconds(anyString(), anyString())).thenReturn(Long.valueOf(10L));
        assertThat(service.getRank(ClassIdType.MANAGEMENT_OVERRIDE, "2018-01-01-00-00-00")).isEqualTo(10L);
    }

    @Test
    public void testGetRankForPriorityId() {
        when(dateProviderMock.getCurrentTime()).thenReturn("2018-01-01-00-00-10");
        when(dateProviderMock.getTimeDifferenceInSeconds(anyString(), anyString())).thenReturn(Long.valueOf(10L));
        assertThat(service.getRank(ClassIdType.PRIORITY, "2018-01-01-00-00-00")).isEqualTo(23L);
    }

    @Test
    public void testGetRankForVipPriorityId() {
        when(dateProviderMock.getCurrentTime()).thenReturn("2018-01-01-00-00-10");
        when(dateProviderMock.getTimeDifferenceInSeconds(anyString(), anyString())).thenReturn(Long.valueOf(10L));
        assertThat(service.getRank(ClassIdType.VIP, "2018-01-01-00-00-00")).isEqualTo(46L);
    }

    @Test
    public void testGetFromTopRequestFromQueueSortsTheDbContentsAndReturnsTheTop() {
        List<QueueEntry> unorderedMockQueueContents = new ArrayList<>();
        QueueEntry qe1 = new QueueEntry(1L, "2018-01-01-00-00-01", ClassIdType.NORMAL, 1L);
        QueueEntry qe2 = new QueueEntry(2L, "2018-01-01-00-00-02", ClassIdType.NORMAL, 2L);

        unorderedMockQueueContents.add(qe1);
        unorderedMockQueueContents.add(qe2);

        when(inMemoryQueueMock.getAllEntries()).thenReturn(unorderedMockQueueContents);
        WorkOrderResponse response = service.getFromTopRequestFromQueue();

        verify(inMemoryQueueMock, times(1)).delete(qe2.getId());

        assertThat(response.getUserId()).isEqualTo(qe2.getId());
        assertThat(response.getDate()).isEqualTo(qe2.getDate());
    }

    @Test
    public void testGetAllEntriesFromQueueReturnsAnEmptyListWhenTheDbIsEmpty() {
        when(inMemoryQueueMock.getAllEntries()).thenReturn(Arrays.asList());
        List<WorkOrderResponse> responses = service.getAllEntries();
        assertThat(responses.size()).isEqualTo(0);
    }

    @Test
    public void testGetAllEntriesFromQueue() {
        List<QueueEntry> unorderedMockQueueContents = new ArrayList<>();
        QueueEntry qe1 = new QueueEntry(1L, "2018-01-01-00-00-01", ClassIdType.NORMAL, 1L);
        QueueEntry qe2 = new QueueEntry(2L, "2018-01-01-00-00-02", ClassIdType.NORMAL, 2L);
        unorderedMockQueueContents.add(qe1);
        unorderedMockQueueContents.add(qe2);

        when(inMemoryQueueMock.getAllEntries()).thenReturn(unorderedMockQueueContents);
        List<WorkOrderResponse> responses = service.getAllEntries();
        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getUserId()).isEqualTo(qe2.getId());
        assertThat(responses.get(1).getUserId()).isEqualTo(qe1.getId());
    }

    @Test
    public void testGetUserPositionFromQueueThrowsNotFoundExceptionWhenIdIsNotInThQueue() {
        when(inMemoryQueueMock.getUserPosition(1L)).thenReturn(-1L);
        Throwable throwable = catchThrowable(() -> service.getUserPositionFromQueue(1L));
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void testGetAverageWaitTimeReturnsZeroWhenTheQueueIsEmpty() {
        assertThat(service.getAverageWaitTime("2018-01-01-00-01-00")).isEqualTo(0L);
    }

    @Test
    public void testGetAverageWaitTimeDoesNotProvideAWaitTimeWhenForEntriesThatWereNotInTheQueueAtTheRequestedTime() {
        String date = "2017-01-01-00-01-00";

        PriorityQueueService priorityQueueService = new PriorityQueueService(inMemoryQueueMock, new DateProvider());
        List<QueueEntry> queue = new ArrayList<>();
        QueueEntry qe1 = new QueueEntry(1L, "2018-01-01-00-00-10", ClassIdType.NORMAL, 1L);
        QueueEntry qe2 = new QueueEntry(2L, "2018-01-01-00-00-20", ClassIdType.NORMAL, 2L);
        queue.add(qe1);
        queue.add(qe2);

        when(inMemoryQueueMock.getAllEntries()).thenReturn(queue);
        assertThat(priorityQueueService.getAverageWaitTime(date)).isEqualTo(0L);
    }

    @Test
    public void testGetAverageWaitTimeReturnsTheCorrectWaitTime() {
        String date = "2018-01-01-00-01-00";

        List<QueueEntry> queue = new ArrayList<>();
        QueueEntry qe1 = new QueueEntry(1L, "2018-01-01-00-00-10", ClassIdType.NORMAL, 1L);
        QueueEntry qe2 = new QueueEntry(2L, "2018-01-01-00-00-20", ClassIdType.NORMAL, 2L);
        queue.add(qe1);
        queue.add(qe2);

        when(inMemoryQueueMock.getAllEntries()).thenReturn(queue);
        when(dateProviderMock.getTimeDifferenceInSeconds(eq("2018-01-01-00-00-10"), eq(date))).thenReturn(50L);
        when(dateProviderMock.getTimeDifferenceInSeconds(eq("2018-01-01-00-00-20"), eq(date))).thenReturn(40L);
        assertThat(service.getAverageWaitTime(date)).isEqualTo(45L);
    }

    @Test
    public void testGetUserPositionFromQueueReturnsSuccessfully() {
        when(inMemoryQueueMock.getUserPosition(1L)).thenReturn(2L);
        assertThat(service.getUserPositionFromQueue(1L)).isEqualTo(2L);
    }

    @Test
    public void testGet95thPercentileWaitTime() {
        PriorityQueueService priorityQueueService = new PriorityQueueService(inMemoryQueueMock, new DateProvider());
        List<QueueEntry> queue = new ArrayList<>();

        queue.add(new QueueEntry(1L, "2018-01-01-00-00-00", ClassIdType.NORMAL, 1L));
        queue.add(new QueueEntry(2L, "2018-01-01-00-00-05", ClassIdType.NORMAL, 2L));
        queue.add(new QueueEntry(3L, "2018-01-01-00-00-10", ClassIdType.PRIORITY, 3L));
        queue.add(new QueueEntry(4L, "2018-01-01-00-00-15", ClassIdType.NORMAL, 4L));
        queue.add(new QueueEntry(5L, "2018-01-01-00-00-20", ClassIdType.VIP, 5L));
        queue.add(new QueueEntry(6L, "2018-01-01-00-00-25", ClassIdType.PRIORITY, 6L));
        queue.add(new QueueEntry(7L, "2018-01-01-00-00-30", ClassIdType.NORMAL, 7L));
        queue.add(new QueueEntry(8L, "2018-01-01-00-00-35", ClassIdType.NORMAL, 8L));
        queue.add(new QueueEntry(9L, "2018-01-01-00-00-40", ClassIdType.PRIORITY, 9L));
        queue.add(new QueueEntry(10L, "2018-01-01-00-00-45", ClassIdType.VIP, 10L));

        when(inMemoryQueueMock.getAllEntries()).thenReturn(queue);
        Long waitTime95thPercentile = priorityQueueService.get95thPercentileWaitTime("2018-01-01-00-01-00");

        assertThat(waitTime95thPercentile).isEqualTo(60L);
    }

    @Test
    public void testGetFromTopRequestFromQueueThrowsNotFoundExceptionWhenTheQueueIsEmpty() {
        when(inMemoryQueueMock.getAllEntries()).thenReturn(Arrays.asList());
        Throwable throwable = catchThrowable(() -> service.getFromTopRequestFromQueue());
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(NotFoundException.class);
    }
}
