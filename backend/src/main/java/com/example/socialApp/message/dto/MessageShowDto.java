package com.example.socialApp.message.dto;

import lombok.Data;

import javax.persistence.Lob;
import java.time.LocalDateTime;

@Data
public class MessageShowDto {

    private Long id;
    private String content;
    @Lob
    private String file;
    private Boolean fileToDownload;
    private LocalDateTime writeDate;
    private LocalDateTime deleteDate;
    private String senderUsername;
    private Long senderId;
    private String avatar;
}
