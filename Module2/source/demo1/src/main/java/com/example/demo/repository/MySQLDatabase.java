package com.example.demo.repository;

import org.springframework.stereotype.Component;

@Component
public class MySQLDatabase implements Database {
    public void save(String data) {
        System.out.println("Saving to MySQL: " + data);
    }
}
