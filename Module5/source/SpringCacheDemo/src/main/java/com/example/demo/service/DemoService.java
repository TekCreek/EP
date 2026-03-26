package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

    private Logger logger = LoggerFactory.getLogger(DemoService.class);

    @Cacheable(value = "answerCache", key = "#questionNumber")
    public String getAnswer(int questionNumber) {

        logger.info("Fetching the question from DB");

        // Simulate delay
        try { Thread.sleep(5000); } catch(InterruptedException e) {}

        logger.info("Sending the response ");

        return "Answer for questionNumber : " + questionNumber + " is `I don't know` ";
    }
}
