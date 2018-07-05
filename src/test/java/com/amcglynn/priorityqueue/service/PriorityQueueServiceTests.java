package com.amcglynn.priorityqueue.service;

import com.amcglynn.priorityqueue.ClassIdType;
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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PriorityQueueServiceTests {

    @Mock
    private InMemoryQueue inMemoryQueueMock;

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

    @Test
    public void testGetTimeDifferenceInSecondsReturnsTheCorrectValue() {
        assertThat(service.getTimeDifferenceInSeconds("2018-07-05-00-00-00", "2018-07-05-00-00-10")).isEqualTo(10L);
    }

    @Test
    public void testGetTimeDifferenceInSecondsReturnsErrorCodeWhenCouldNotParseText() {
        assertThat(service.getTimeDifferenceInSeconds("2018-07-05-00-0000", "2018-07-05-00-00-10")).isEqualTo(-1);
    }
}
