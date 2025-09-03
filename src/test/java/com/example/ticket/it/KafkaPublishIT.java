package com.example.ticket.it;

import com.example.ticket.dto.SupportTicketRequest;
import com.example.ticket.messaging.TicketEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "app.kafka.topic=ticket-events"
})
@EmbeddedKafka(partitions = 1, topics = { "ticket-events" })
class KafkaPublishIT {

    @Autowired private org.springframework.test.web.reactive.server.WebTestClient webClient;
    @Autowired private EmbeddedKafkaBroker broker;
    @Autowired private ObjectMapper om;

    @Test
    void post_creates_ticket_and_emits_event() {
        // Subscribe a test consumer to the topic
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", broker);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        var cf = new DefaultKafkaConsumerFactory<>(
                consumerProps, new StringDeserializer(), new JsonDeserializer<>(TicketEvent.class, false));
        var consumer = cf.createConsumer();
        broker.consumeFromAnEmbeddedTopic(consumer, "ticket-events");

        // Call API
        var req = new SupportTicketRequest("Integration", "Kafka");
        webClient.post().uri("/tickets").bodyValue(req).exchange().expectStatus().isCreated();

        // Read one event
        ConsumerRecord<String, TicketEvent> record = KafkaTestUtils.getSingleRecord(consumer, "ticket-events");
        assertThat(record.value()).isNotNull();
        assertThat(record.value().eventType().name()).isEqualTo("CREATED");
        assertThat(record.value().payload().title()).isEqualTo("Integration");
    }
}
