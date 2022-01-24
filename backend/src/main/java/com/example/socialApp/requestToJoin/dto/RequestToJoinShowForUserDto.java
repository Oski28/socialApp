package com.example.socialApp.requestToJoin.dto;

import com.example.socialApp.requestToJoin.model_repo.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestToJoinShowForUserDto {

    private Long id;
    private Long eventId;
    private String eventName;
    private LocalDateTime date;
    private Status status;
    private LocalDateTime addDate;
}
