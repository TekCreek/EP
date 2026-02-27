package com.example.demo.model;

import lombok.Data;

@Data
public class MessageData {
    String targetEmail;
    String subject;
    String content;
}
