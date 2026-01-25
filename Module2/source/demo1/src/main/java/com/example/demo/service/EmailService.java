package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void sendWelcomeEmail(String username) {
        System.out.println("Sending welcome email to: " + username);
    }
}
