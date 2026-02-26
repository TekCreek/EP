package com.example.demo.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
@EnableJms
public class JmsConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String username;

    @Value("${spring.activemq.password}")
    private String password;

    // ---------------------------------------------------------------
    // ConnectionFactory — the base connection to the broker
    // ---------------------------------------------------------------
    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(brokerUrl);
        factory.setUserName(username);
        factory.setPassword(password);
        // Trust all packages for object deserialization (restrict in production)
        factory.setTrustAllPackages(true);
        return factory;
    }

    // ---------------------------------------------------------------
    // JmsTemplate — for sending messages (queues)
    // ---------------------------------------------------------------
    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate(connectionFactory());
        template.setMessageConverter(messageConverter());
        template.setDeliveryPersistent(true);
        template.setPubSubDomain(false); // false = Queue, true = Topic
        return template;
    }

    // ---------------------------------------------------------------
    // Queue listener container factory
    // ---------------------------------------------------------------
    @Bean
    public JmsListenerContainerFactory<?> queueListenerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setMessageConverter(messageConverter());
        factory.setConcurrency("3-10");         // min-max consumer threads
        factory.setSessionAcknowledgeMode(1);   // 1 = AUTO_ACKNOWLEDGE
        factory.setPubSubDomain(false);          // Queue mode
        factory.setErrorHandler(t ->
                System.err.println("JMS error: " + t.getMessage()));
        return factory;
    }


    // ---------------------------------------------------------------
    // Message converter — serializes objects to/from JSON automatically
    // ---------------------------------------------------------------
    @Bean
    public MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);  // Send as TextMessage (JSON string)
        converter.setTypeIdPropertyName("_type");   // Header used to identify class on receive
        return converter;
    }
}
