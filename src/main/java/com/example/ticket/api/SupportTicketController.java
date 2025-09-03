package com.example.ticket.api;

import com.example.ticket.dto.SupportTicketRequest;
import com.example.ticket.dto.SupportTicketResponse;
import com.example.ticket.dto.SupportTicketUpdateRequest;
import com.example.ticket.service.SupportTicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class SupportTicketController {

    private final SupportTicketService service;

    @PostMapping
    public ResponseEntity<SupportTicketResponse> create(@Valid @RequestBody SupportTicketRequest req) {
        SupportTicketResponse created = service.create(req);
        return ResponseEntity
                .created(URI.create("/tickets/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public SupportTicketResponse get(@PathVariable UUID id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public SupportTicketResponse update(@PathVariable UUID id, @Valid @RequestBody SupportTicketUpdateRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

