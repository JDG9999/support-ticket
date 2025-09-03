# Support Ticket Microservice

A Spring Boot microservice that manages `SupportTicket` entities with full CRUD operations and publishes events to a Kafka topic. Includes unit tests, integration tests with embedded Kafka, and an 80%+ code coverage gate using JaCoCo.

---

## Features

- **SupportTicket CRUD API**
  - `POST /tickets` – Create a ticket
  - `GET /tickets/{id}` – Fetch a ticket by ID
  - `PUT /tickets/{id}` – Update a ticket
  - `DELETE /tickets/{id}` – Delete a ticket

- **Kafka Event Publishing**
  - Publishes `ticket-events` messages on create, update, and delete.
  - Message format:
    ```json
    {
      "eventType": "CREATED" | "UPDATED" | "DELETED",
      "ticketId": "UUID",
      "timestamp": "ISO8601",
      "payload": {
        "id": "UUID",
        "title": "...",
        "description": "...",
        "status": "OPEN|IN_PROGRESS|RESOLVED",
        "createdAt": "ISO8601",
        "updatedAt": "ISO8601"
      }
    }
    ```

- **In-memory H2 database** for fast local development.
- **Tests**:
  - Unit tests for controller and service layers (Mockito)
  - Integration test using Embedded Kafka to verify event publishing
  - Minimum 80% code coverage enforced with JaCoCo

---

## Prerequisites

- Java 17+
- Maven 3.9+
- Docker (for running Kafka locally)

---

## Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/your-username/support-ticket.git
cd support-ticket```

### 2. Clone the repository
```docker compose up -d```

### 3. Run the application
```mvn spring-boot:run```

### 4. Run all tests
```mvn clean verify```

### 5. Generate coverage report
```open target/site/jacoco/index.html```

##EXAMPLES

### Create a ticket
```curl -X POST http://localhost:8080/tickets \
  -H "Content-Type: application/json" \
  -d '{"title":"My ticket","description":"This is a ticket"}'```

### Get a ticket
```curl http://localhost:8080/tickets/{id}```

### Update a ticket
```curl -X PUT http://localhost:8080/tickets/{id} \
  -H "Content-Type: application/json" \
  -d '{"status":"IN_PROGRESS"}'```

### Delete a ticket
```curl -X DELETE http://localhost:8080/tickets/{id} -i```


