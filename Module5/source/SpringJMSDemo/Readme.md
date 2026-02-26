# Demo Application for JMS with Spring

## Launching the Application

```bash

    # Start the ActiveMQ broker
    docker compose -f ../docker/config/docker-compose.yml up -d

    # Start the Spring Boot application
    mvn spring-boot:run

    # OR 
    # Open the project in IntelliJ and run the main class: MessagingApplication.java
```

## Testing the Application

```bash
    # Hitting the post endpoint to send a message
    curl -X POST http://localhost:8080/api/messages -H "Content-Type: application/json" -d '{"message": "Hello, JMS!"}'
```