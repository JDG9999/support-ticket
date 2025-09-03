package com.example.ticket.messaging;

import com.example.ticket.dto.SupportTicketResponse;
import java.time.Instant;
import java.util.UUID;

public record TicketEvent(
        EventType eventType,
        UUID ticketId,
        Instant timestamp,
        SupportTicketResponse payload
) {}

