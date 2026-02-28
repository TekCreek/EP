package com.example.demo.controller;

import com.example.demo.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    DemoService demoService;

    @GetMapping("/hello/{name}")
    public String sayHello(@PathVariable("name")String name) {

        logger.debug("DEBUG: Controller Received Hello from {}", name);
        logger.info("INFO: Controller Received Hello from {}", name);
        logger.warn("WARN: Controller Received Hello from {}", name);
        logger.error("ERROR: Controller Received Hello from {}", name);

        demoService.log(name);

        return "Hi !! " + name;
    }
}
