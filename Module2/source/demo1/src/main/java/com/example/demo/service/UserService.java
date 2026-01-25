package com.example.demo.service;

import com.example.demo.repository.Database;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final Database database;
    private final EmailService emailService;

    public UserService(Database database, EmailService emailService) {
        this.database = database;
        this.emailService = emailService;
    }

    public void createUser(String username) {
        database.save(username);
        emailService.sendWelcomeEmail(username);
    }
}