package com.example.demo.config;

import com.example.demo.repository.Database;
import com.example.demo.repository.MySQLDatabase;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Database database() {
        return new MySQLDatabase();
    }
}
