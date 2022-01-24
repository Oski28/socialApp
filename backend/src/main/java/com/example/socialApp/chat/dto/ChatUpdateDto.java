package com.example.socialApp.chat.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ChatUpdateDto {

    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 3, max = 50, message = "Name must contain between 3 and 50 characters.")
    private String name;
}
