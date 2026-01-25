package com.example.mvcdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MvcdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvcdemoApplication.class, args);
		System.out.println("\n========================================");
		System.out.println("Student Management System Started!");
		System.out.println("Access the application at:");
		System.out.println("http://localhost:8080");
		System.out.println("========================================\n");
	}

}
