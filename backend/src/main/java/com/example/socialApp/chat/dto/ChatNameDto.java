package com.example.socialApp.chat.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatNameDto {

    private Long id;
    private String name;
    private LocalDateTime lastMessage;
}
