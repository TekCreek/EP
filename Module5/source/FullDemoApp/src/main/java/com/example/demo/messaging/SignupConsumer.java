package com.example.demo.messaging;

import com.example.demo.model.MessageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class SignupConsumer {

    Logger logger = LoggerFactory.getLogger(SignupConsumer.class);

    @JmsListener(destination = JMSDestination.SIGNUP_QUEUE)
    public void onMessage(MessageData message) {
        logger.info("Signup message received : ");
        logger.info("Target Email : {}", message.getTargetEmail());
        logger.info("Subject: {}", message.getSubject());
        logger.info("Content: {}", message.getContent());
    }
}
