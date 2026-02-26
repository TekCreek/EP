package com.example.demo.api;

import com.example.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DemoController {

    @Autowired
    DemoService service;

    @GetMapping("/answer")
    public String getAnswer(@RequestParam("qno") int questionNumber) {
        return service.getAnswer(questionNumber);
    }

}
