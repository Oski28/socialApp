package com.example.socialApp.requestToJoin.dto;

import com.example.socialApp.requestToJoin.model_repo.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestToJoinShowForEventDto {

    private Long id;
    private String avatar;
    private Long userId;
    private String username;
    private Status status;
    private LocalDateTime addDate;
}
