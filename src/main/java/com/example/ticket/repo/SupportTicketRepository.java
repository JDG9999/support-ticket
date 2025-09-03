package com.example.ticket.repo;

import com.example.ticket.entity.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, UUID> {
}
