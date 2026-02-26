package com.example.demo.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

    @Cacheable(value = "answerCache", key = "#questionNumber")
    public String getAnswer(int questionNumber) {

        // Simulate delay
        try { Thread.sleep(5000); } catch(InterruptedException e) {}

        return "Answer for questionNumber : " + questionNumber;
    }
}
