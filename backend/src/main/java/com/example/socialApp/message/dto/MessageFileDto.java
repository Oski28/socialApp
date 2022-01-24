package com.example.socialApp.message.dto;

import lombok.Data;

@Data
public class MessageFileDto {
    private String fileType;
    private String fileName;
    private byte[] fileData;
}
