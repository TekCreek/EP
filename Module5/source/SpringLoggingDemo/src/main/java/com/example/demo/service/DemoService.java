package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DemoService {
    Logger logger = LoggerFactory.getLogger(DemoService.class);

    public void log(String name) {
        logger.debug("DEBUG: Service Received Hello from {}", name);
        logger.info("INFO: Service Received Hello from {}", name);
        logger.warn("WARN: Service Received Hello from {}", name);
        logger.error("ERROR: Service Received Hello from {}", name);
    }
}
