package org.example.messaging;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class JMSProducer {
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

            // Create a message producer
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create a text message
            TextMessage message = session.createTextMessage("Hello, JMS!");

            // Send the message
            producer.send(message);
            System.out.println("Sent message: " + message.getText());

            // Clean up
            producer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

