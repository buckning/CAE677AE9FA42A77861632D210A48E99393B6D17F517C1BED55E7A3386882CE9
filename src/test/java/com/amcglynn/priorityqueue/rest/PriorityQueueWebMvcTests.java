package com.amcglynn.priorityqueue.rest;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    private String date = "2018-01-01-00-00-00";

    @Test
    public void testEnqueueEndpointReturnsSuccessfully() throws Exception {
        String request = new ObjectMapper().writeValueAsString(new WorkOrderRequest(1L, date));
        mockMvc.perform(post("/queue")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(equalTo(request)));
    }

    @Test
    public void testDeleteEndpointReturnsSuccessfully() throws Exception {
        String userId = "1";
        mockMvc.perform(delete("/queue/" + userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    public void testGetTopIdFromQueueReturnsSuccessfully() throws Exception {
        mockMvc.perform(get("/queue/top")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(equalTo(new ObjectMapper()
                        .writeValueAsString(new WorkOrderRequest(1L, "01012018")))));
    }

    @Test
    public void testGetIdFromQueueReturnsSuccessfully() throws Exception {
        String id = "1";
        mockMvc.perform(get("/queue/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(equalTo(new ObjectMapper()
                .writeValueAsString(new WorkOrderRequest(1L, "01012018")))));
    }

    @Test
    public void testGetAllIdsFromQueueReturnsSuccessfully() throws Exception {
        mockMvc.perform(get("/queue")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void testDeleteEndpointReturns400BadRequestWhenUserIdIsGreaterThanMax() throws Exception {
        verifyBadRequestByDeleteFromQueueEndpoint("9223372036854775808");
    }

    @Test
    public void testDeleteEndpointReturns400BadRequestWhenUserIdIsNotALong() throws Exception {
        verifyBadRequestByDeleteFromQueueEndpoint("baddata");
    }

    @Test
    public void testDeleteEndpointReturns400BadRequestWhenUserIdIsNull() throws Exception {
        verifyBadRequestByDeleteFromQueueEndpoint(null);
    }

    private void verifyBadRequestByDeleteFromQueueEndpoint(String userId) throws Exception {
        mockMvc.perform(delete("/queue/" + userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void testPostToQueueEndpointReturns400BadRequestWhenIdIsLessThanAllowed() throws Exception {
        verifyBadRequestIsReturnedByQueueEndpoint(new ObjectMapper()
                .writeValueAsString(new WorkOrderRequest(0L, "01012018")));
    }

    @Test
    public void testPostToQueueEndpointReturns400BadRequestWhenIdIsGreaterThanAllowed() throws Exception {
        verifyBadRequestIsReturnedByQueueEndpoint("{\"userId\": 9223372036854775808, \"date\": \"01012018\"}");
    }

    @Test
    public void testPostToQueueEndpointReturns400BadRequestWhenIdIsNotInRequest() throws Exception {
        verifyBadRequestIsReturnedByQueueEndpoint("{\"date\": \"01012018\"}");
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
