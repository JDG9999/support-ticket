package com.example.ticket.api.dto;

import com.example.ticket.domain.TicketStatus;
import jakarta.validation.constraints.Size;

public record SupportTicketUpdateRequest(
        @Size(max = 255) String title,
        @Size(max = 4000) String description,
        TicketStatus status
) {}

