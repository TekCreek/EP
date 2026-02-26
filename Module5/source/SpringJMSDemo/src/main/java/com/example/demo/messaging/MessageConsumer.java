package com.example.demo.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {
    @JmsListener(destination = JMSDestination.ORDER_QUEUE)
    public void onMessage(String message) {
        System.out.println("Received: " + message);
    }
}
