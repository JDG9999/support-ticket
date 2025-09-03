package com.example.ticket.dto;

import com.example.ticket.entity.TicketStatus;
import jakarta.validation.constraints.Size;

public record SupportTicketUpdateRequest(
        @Size(max = 255) String title,
        @Size(max = 4000) String description,
        TicketStatus status
) {}

