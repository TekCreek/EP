package org.example.messaging.pubsub;
import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class JMSSubscriber {
    public static void main(String[] args) {
        // Create a connection factory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        try {
            // Create a connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create a topic
            Destination destination = session.createTopic("TEST.TOPIC");

            // Create a message consumer
            MessageConsumer consumer = session.createConsumer(destination);

            // Receive messages in a loop
            while (true) {
                Message message = consumer.receive(); // Wait for 1 seconds

                if (message == null) {
                    break; // this never happens
                }
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    System.out.println("Received message: " + textMessage.getText());
                } else {
                    System.out.println("Received non-text message");
                }
            }

            // Clean up
            consumer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}