package com.example.socialApp.message.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MessageDto {


    @Size(max = 300, message = "Message content must be max 300 characters.")
    private String content;

    @NotNull
    private Long userId;

    private String file;

    private String fileName;
}
