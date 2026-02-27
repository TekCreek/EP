package com.example.demo.messaging;

import com.example.demo.model.MessageData;
import com.example.demo.model.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class MessageProducer {

    private JmsTemplate jmsTemplate;

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendSignupMessage(UserVO user) {
        MessageData messageData = new MessageData();
        messageData.setTargetEmail(user.getEmail());
        messageData.setSubject("Welcome!! ");
        messageData.setContent("Hello ! " + user.getUsername());
        jmsTemplate.convertAndSend(JMSDestination.SIGNUP_QUEUE, messageData);
    }
}
