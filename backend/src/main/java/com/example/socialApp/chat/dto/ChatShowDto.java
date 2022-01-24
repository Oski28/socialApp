package com.example.socialApp.chat.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ChatShowDto {

    private Long id;
    private String name;
    private Set<String> usersName;
    private String eventName;
}
