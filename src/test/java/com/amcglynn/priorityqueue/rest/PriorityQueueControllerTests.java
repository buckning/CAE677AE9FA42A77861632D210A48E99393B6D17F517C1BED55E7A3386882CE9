package com.amcglynn.priorityqueue.rest;

import com.amcglynn.priorityqueue.exceptions.BadRequestException;
import com.amcglynn.priorityqueue.exceptions.ConflictException;
import com.amcglynn.priorityqueue.responses.GetPositionResponse;
import com.amcglynn.priorityqueue.service.PriorityQueueService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PriorityQueueControllerTests {

    @Mock
    private PriorityQueueService serviceMock;

    @InjectMocks
    private PriorityQueueController controller;

    @Test
    public void testDeleteFromQueueEndpointCompletesSuccessfully() throws Exception {
        controller.deleteFromQueue(1L);
        verify(serviceMock, times(1)).removeFromQueue(1L);
    }

    @Test
    public void testGetIdFromQueueReturns400BadRequestWhenUserIdIsLessThanTheMinimumValue() throws Exception {
        Throwable throwable = catchThrowable(() -> controller.getIdFromQueue(0L));
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(BadRequestException.class);
    }

    @Test
    public void testGetIdFromQueueCompletesSuccessfully() throws Exception {
        when(serviceMock.getUserPositionFromQueue(1L)).thenReturn(5L);
        GetPositionResponse response = controller.getIdFromQueue(1L);
        assertThat(response.getPosition()).isEqualTo(5L);
    }

    @Test
    public void testDeleteFromQueueEndpointReturns400BadRequestWhenUserIdIsLessThanTheMinimumValue() throws Exception {
        Throwable throwable = catchThrowable(() -> controller.deleteFromQueue(0L));
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(BadRequestException.class);
    }

    @Test
    public void testDeleteFromQueueEndpointReturns400BadRequestWhenUserIdIsANegativeNumber() throws Exception {
        Throwable throwable = catchThrowable(() -> controller.deleteFromQueue(-1L));
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(BadRequestException.class);
    }
}
