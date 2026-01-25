package com.example.mvcdemo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * CONTROLLER - Home Controller
 * Handles home page requests
 */
@Controller
public class HomeController {

    /**
     * Home page
     * URL: GET /
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Home");
        model.addAttribute("welcomeMessage",
                "Welcome to Student Management System");

        return "home";  // Resolves to /WEB-INF/views/home.jsp
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
