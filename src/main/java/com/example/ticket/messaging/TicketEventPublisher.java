package com.example.ticket.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketEventPublisher {
    private final KafkaTemplate<String, TicketEvent> kafkaTemplate;

    @Value("${app.kafka.topic:ticket-events}")
    private String topic;

    public void publish(TicketEvent event) {
        UUID key = event.ticketId();
        kafkaTemplate.send(topic, key.toString(), event);
    }
}

