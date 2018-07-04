package com.amcglynn.priorityqueue;

import com.amcglynn.priorityqueue.requests.WorkOrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PriorityQueueWebMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testEnqueueEndpointReturnsSuccessfully() throws Exception {
        String request = new ObjectMapper().writeValueAsString(new WorkOrderRequest(1L, "today"));
        mockMvc.perform(post("/queue")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(equalTo(request)));
    }

    @Test
    public void testPostToQueueEndpointReturns400BadRequestWhenIdIsLessThanAllowed() throws Exception {
        verifyBadRequestIsReturnedByQueueEndpoint(new ObjectMapper()
                .writeValueAsString(new WorkOrderRequest(0L, "today")));
    }

    @Test
    public void testPostToQueueEndpointReturns400BadRequestWhenIdIsGreaterThanAllowed() throws Exception {
        verifyBadRequestIsReturnedByQueueEndpoint("{\"userId\": 9223372036854775808, \"date\": \"today\"}");
    }

    @Test
    public void testPostToQueueEndpointReturns400BadRequestWhenIdIsNotInRequest() throws Exception {
        verifyBadRequestIsReturnedByQueueEndpoint("{\"date\": \"today\"}");
    }

    @Test
    public void testPostToQueueEndpointReturns400BadRequestWhenDateIsNotInRequest() throws Exception {
        verifyBadRequestIsReturnedByQueueEndpoint("{\"userId\": 1}");
    }

    private void verifyBadRequestIsReturnedByQueueEndpoint(String badRequest) throws Exception {
        mockMvc.perform(post("/queue")
                .content(badRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest());
    }
}
