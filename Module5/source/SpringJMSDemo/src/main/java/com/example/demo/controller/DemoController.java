package com.example.demo.controller;

import com.example.demo.messaging.JMSDestination;
import com.example.demo.messaging.MessageProducer;
import com.example.demo.model.PostPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {

    @Autowired
    MessageProducer msgProducer;

    @PostMapping("/messages")
    public String postMessage(@RequestBody PostPayload request) {
        msgProducer.send(
                JMSDestination.ORDER_QUEUE,
                "New Order received with message - " + request.getMessage());
        return "SUCCESS";
    }
}
