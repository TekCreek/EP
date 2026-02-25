package org.example.messaging;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
public class JMSConsumer {
    public static void main(String[] args) {
        // Create a connection factory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        try {
            // Create a connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create a queue
            Destination destination = session.createQueue("TEST.QUEUE");

            // Create a message consumer
            MessageConsumer consumer = session.createConsumer(destination);

            // Receive messages in a loop
            while (true) {
                Message message = consumer.receive(1000); // Wait for 1 seconds

                // Optionally, you can use consumer.receive() without a timeout to wait indefinitely for messages
                if (message == null) {
                    System.out.println("No more messages to receive. Exiting.");
                    break; // Exit the loop if no message is received
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