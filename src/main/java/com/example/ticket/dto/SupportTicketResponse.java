package com.example.ticket.dto;

import com.example.ticket.entity.TicketStatus;
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
