package com.example.ticket.api.dto;

import com.example.ticket.domain.TicketStatus;
import java.time.Instant;
import java.util.UUID;

public record SupportTicketResponse(
        UUID id,
        String title,
        String description,
        TicketStatus status,
        Instant createdAt,
        Instant updatedAt
) {}
