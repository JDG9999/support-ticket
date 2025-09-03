package com.example.ticket.service;

import java.util.UUID;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(UUID id) { super("Ticket not found: " + id); }
}

