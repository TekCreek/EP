# ActiveMQ Docker Setup (Fixed)

## What was broken & why

Three issues prevented the web console from opening:

1. **Custom `jetty.xml` was mounted** — ActiveMQ 5.18.x bundles its own `jetty.xml`
   that wires the `/admin`, `/api`, and `/fileserver` webapps. Replacing it with a
   bare Jetty config removed all webapp deployments, so port 8161 returned nothing.

2. **`<import resource="jetty.xml"/>` in `activemq.xml`** — This caused Jetty to
   start twice (once from Spring and once from the broker's internal launcher),
   causing port conflicts and startup failures.

3. **`-Djetty.host` not set** — Without this, Jetty binds to `127.0.0.1` inside
   the container, making port 8161 unreachable from the host.

### What was fixed
- Removed `jetty.xml` from mounts entirely (ActiveMQ uses its built-in one)
- Removed the `<import resource="jetty.xml"/>` line from `activemq.xml`
- Added `-Djetty.host=0.0.0.0` to `ACTIVEMQ_OPTS` so the console binds to all interfaces

---

## Quick Start

```bash
# Start ActiveMQ
docker compose up -d

# Watch startup logs (wait for "ActiveMQ started")
docker compose logs -f activemq

# Stop
docker compose down

# Stop and wipe all persisted data
docker compose down -v
```

## Access Points

| Service          | URL / Address                     | Credentials      |
|------------------|-----------------------------------|------------------|
| **Web Console**  | http://localhost:8161/admin       | admin / admin    |
| REST API         | http://localhost:8161/api         | admin / admin    |
| OpenWire (JMS)   | tcp://localhost:61616             | —                |
| STOMP            | tcp://localhost:61613             | —                |
| STOMP/WebSocket  | ws://localhost:61614              | —                |
| MQTT             | tcp://localhost:1883              | —                |
| AMQP             | amqp://localhost:5672             | —                |

## Users

| Username   | Password      | Roles                          |
|------------|---------------|--------------------------------|
| admin      | admin         | admins, publishers, consumers  |
| publisher  | publisher123  | publishers                     |
| consumer   | consumer123   | consumers                      |

## Pre-configured Destinations

**Queues:** `queue.orders`, `queue.notifications`
**Topics:** `topic.events`, `topic.alerts`
**DLQ pattern:** `DLQ.<queue-name>` (auto-created on message failure)

## Configuration Files

| File                              | Purpose                                         |
|-----------------------------------|-------------------------------------------------|
| `docker-compose.yml`              | Container, ports, volumes, env vars             |
| `config/activemq.xml`            | Broker: queues, auth, persistence, limits       |
| `config/jetty-realm.properties`  | Web console login credentials                   |
| `config/log4j2.properties`       | Logging levels and rolling file output          |

> `jetty.xml` is intentionally NOT mounted — ActiveMQ manages it internally.

## Spring Boot Integration

### pom.xml
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-activemq</artifactId>
</dependency>
```

### application.yml
```yaml
spring:
  activemq:
    broker-url: tcp://localhost:61616
    user: publisher
    password: publisher123
    pool:
      enabled: true
      max-connections: 10
  jms:
    template:
      default-destination: queue.orders
      delivery-mode: persistent
    listener:
      acknowledge-mode: auto
```

### Producer
```java
@Service
public class MessageProducer {
    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(String destination, Object message) {
        jmsTemplate.convertAndSend(destination, message);
    }
}
```

### Consumer
```java
@Component
public class MessageConsumer {
    @JmsListener(destination = "queue.orders")
    public void onMessage(String message) {
        System.out.println("Received: " + message);
    }
}
```

## Health Check

```bash
# Verify broker is up
curl -u admin:admin http://localhost:8161/api/brokers

# Browse queue messages
curl -u admin:admin "http://localhost:8161/api/message/queue.orders?browse=true"
```
