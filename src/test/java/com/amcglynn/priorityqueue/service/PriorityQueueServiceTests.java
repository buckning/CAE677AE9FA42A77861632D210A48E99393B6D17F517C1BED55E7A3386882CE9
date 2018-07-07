package com.amcglynn.priorityqueue.service;

import com.amcglynn.priorityqueue.ClassIdType;
import com.amcglynn.priorityqueue.DateProvider;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import static org.mockito.ArgumentMatchers.anyString;
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
        Throwable throwable = catchThrowable(() -> service.createEntryInQueue(1L, "01012018"));
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(ConflictException.class);
    }

    @Test
    public void testCreateEntryInQueueCompletesSuccessfully() {
        Long userId = 1L;
        String date = "2018-01-01-00-00-00";
        when(dateProviderMock.getCurrentTime()).thenReturn("2018-01-01-00-00-10");
        when(dateProviderMock.getTimeDifferenceInSeconds(anyString(), anyString())).thenReturn(new Long(10L));

        WorkOrderResponse response = service.createEntryInQueue(userId, date);

        verify(inMemoryQueueMock, times(1)).create(1L, date, ClassIdType.NORMAL, 10L);
        assertThat(response.getDate()).isEqualTo(date);
        assertThat(response.getRank()).isEqualTo(10L);
        assertThat(response.getUserId()).isEqualTo(1L);
    }

    @Test
    public void testGetRankReturnsTheNumberOfSecondsInTheQueueForNormalId() {
        when(dateProviderMock.getCurrentTime()).thenReturn("2018-01-01-00-00-10");
        when(dateProviderMock.getTimeDifferenceInSeconds(anyString(), anyString())).thenReturn(new Long(10L));
        assertThat(service.getRank(ClassIdType.NORMAL, "2018-01-01-00-00-00")).isEqualTo(10L);
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
    public void testGetFromTopRequestFromQueueThrowsNotFoundExceptionWhenTheQueueIsEmpty() {
        when(inMemoryQueueMock.getAllEntries()).thenReturn(Arrays.asList());
        Throwable throwable = catchThrowable(() -> service.getFromTopRequestFromQueue());
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void testGetIdClassForNormalId() {
        assertThat(service.getClassId(1L)).isEqualTo(ClassIdType.NORMAL);
    }

    @Test
    public void testGetIdClassForPriorityId() {
        assertThat(service.getClassId(3L)).isEqualTo(ClassIdType.PRIORITY);
    }

    @Test
    public void testGetIdClassForVipId() {
        assertThat(service.getClassId(5L)).isEqualTo(ClassIdType.VIP);
    }

    @Test
    public void testGetIdClassForManagementOverrideId() {
        assertThat(service.getClassId(15L)).isEqualTo(ClassIdType.MANAGEMENT_OVERRIDE);
    }
}
