package com.example.ticket.service;

import com.example.ticket.api.SupportTicketMapper;
import com.example.ticket.api.dto.SupportTicketRequest;
import com.example.ticket.api.dto.SupportTicketUpdateRequest;
import com.example.ticket.domain.SupportTicket;
import com.example.ticket.domain.TicketStatus;
import com.example.ticket.messaging.TicketEventPublisher;
import com.example.ticket.repo.SupportTicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupportTicketServiceTest {

    @Mock SupportTicketRepository repo;
    @Mock TicketEventPublisher publisher;
    @InjectMocks SupportTicketService service;
    @Spy SupportTicketMapper mapper = new SupportTicketMapper();

    @Test
    void create_persists_and_publishes() {
        var req = new SupportTicketRequest("Title", "Desc");
        when(repo.save(any())).thenAnswer(inv -> {
            SupportTicket t = inv.getArgument(0);
            t.setId(UUID.randomUUID());
            t.setStatus(TicketStatus.OPEN);
            t.setCreatedAt(java.time.Instant.now());
            t.setUpdatedAt(t.getCreatedAt());
            return t;
        });

        var resp = service.create(req);

        assertThat(resp.id()).isNotNull();
        verify(repo).save(any(SupportTicket.class));
        verify(publisher).publish(any());
    }

    @Test
    void get_not_found_throws() {
        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());
        assertThrows(TicketNotFoundException.class, () -> service.get(id));
    }

    @Test
    void update_modifies_and_publishes() {
        UUID id = UUID.randomUUID();
        var existing = SupportTicket.builder().id(id).title("A").description("B")
                .status(TicketStatus.OPEN).createdAt(java.time.Instant.now()).updatedAt(java.time.Instant.now()).build();
        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var resp = service.update(id, new SupportTicketUpdateRequest("A2", "B2", TicketStatus.IN_PROGRESS));

        assertThat(resp.title()).isEqualTo("A2");
        assertThat(resp.status()).isEqualTo(TicketStatus.IN_PROGRESS);
        verify(publisher).publish(any());
    }
}
