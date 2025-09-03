package com.example.ticket.api;

import com.example.ticket.entity.TicketStatus;
import com.example.ticket.dto.SupportTicketRequest;
import com.example.ticket.dto.SupportTicketResponse;
import com.example.ticket.service.SupportTicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SupportTicketController.class)
class SupportTicketControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockBean
    SupportTicketService service;

    @Test
    void create_valid_201() throws Exception {
        var id = UUID.randomUUID();
        var resp = new SupportTicketResponse(id, "T", "D", TicketStatus.OPEN, Instant.now(), Instant.now());
        Mockito.when(service.create(Mockito.any())).thenReturn(resp);

        mvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new SupportTicketRequest("T", "D"))))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/tickets/" + id))
                .andExpect(jsonPath("$.id", is(id.toString())));
    }

    @Test
    void create_invalid_400() throws Exception {
        mvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new SupportTicketRequest("", ""))))
                .andExpect(status().isBadRequest());
    }
}

