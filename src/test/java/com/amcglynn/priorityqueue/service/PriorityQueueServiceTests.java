package com.amcglynn.priorityqueue.service;

import com.amcglynn.priorityqueue.ClassIdType;
import com.amcglynn.priorityqueue.DateProvider;
import com.amcglynn.priorityqueue.dal.InMemoryQueue;
import com.amcglynn.priorityqueue.exceptions.ConflictException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        service.createEntryInQueue(1L, "01012018");
        verify(inMemoryQueueMock, times(1)).create(1L, "01012018");
    }

    @Test
    public void testGetRankReturnsTheNumberOfSecondsInTheQueueForNormalId() {
        when(dateProviderMock.getCurrentTime()).thenReturn("2018-01-01-00-00-10");
        when(dateProviderMock.getTimeDifferenceInSeconds(anyString(), anyString())).thenReturn(new Long(10L));
        assertThat(service.getRank(ClassIdType.NORMAL, "2018-01-01-00-00-00")).isEqualTo(10L);
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
