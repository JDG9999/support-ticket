package com.example.ticket.service;

import com.example.ticket.util.SupportTicketMapper;
import com.example.ticket.entity.SupportTicket;
import com.example.ticket.dto.SupportTicketRequest;
import com.example.ticket.dto.SupportTicketResponse;
import com.example.ticket.dto.SupportTicketUpdateRequest;
import com.example.ticket.messaging.*;
import com.example.ticket.repo.SupportTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupportTicketService {

    private final SupportTicketRepository repo;
    private final SupportTicketMapper mapper;
    private final TicketEventPublisher publisher;

    @Transactional
    public SupportTicketResponse create(SupportTicketRequest req) {
        SupportTicket t = SupportTicket.builder()
                .title(req.title())
                .description(req.description())
                .build();
        t = repo.save(t);
        SupportTicketResponse resp = mapper.toResponse(t);
        publisher.publish(new TicketEvent(EventType.CREATED, t.getId(), Instant.now(), resp));
        return resp;
    }

    @Transactional(readOnly = true)
    public SupportTicketResponse get(UUID id) {
        SupportTicket t = repo.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
        return mapper.toResponse(t);
    }

    @Transactional
    public SupportTicketResponse update(UUID id, SupportTicketUpdateRequest req) {
        SupportTicket t = repo.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
        mapper.applyUpdate(t, req);
        t = repo.save(t);
        SupportTicketResponse resp = mapper.toResponse(t);
        publisher.publish(new TicketEvent(EventType.UPDATED, t.getId(), Instant.now(), resp));
        return resp;
    }

    @Transactional
    public void delete(UUID id) {
        SupportTicket t = repo.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
        repo.delete(t);
        publisher.publish(new TicketEvent(EventType.DELETED, id, Instant.now(),
                new SupportTicketResponse(id, t.getTitle(), t.getDescription(), t.getStatus(), t.getCreatedAt(), t.getUpdatedAt())));
    }
}
