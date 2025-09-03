package com.example.ticket.api;

import com.example.ticket.api.dto.*;
import com.example.ticket.domain.SupportTicket;
import org.springframework.stereotype.Component;

@Component
public class SupportTicketMapper {
    public SupportTicketResponse toResponse(SupportTicket t) {
        return new SupportTicketResponse(t.getId(), t.getTitle(), t.getDescription(),
                t.getStatus(), t.getCreatedAt(), t.getUpdatedAt());
    }

    public void applyUpdate(SupportTicket t, SupportTicketUpdateRequest req) {
        if (req.title() != null) t.setTitle(req.title());
        if (req.description() != null) t.setDescription(req.description());
        if (req.status() != null) t.setStatus(req.status());
    }
}
