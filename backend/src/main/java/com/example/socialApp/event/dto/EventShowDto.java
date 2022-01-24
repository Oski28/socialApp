package com.example.socialApp.event.dto;

import com.example.socialApp.user.dto.UserEventDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class EventShowDto {

    private Long id;
    private String name;
    private String description;
    private Integer ageLimit;
    private Integer maxNumberOfParticipant;
    private String location;
    private LocalDateTime date;
    private Boolean freeJoin;
    private Set<String> categories;
    private String organizer;
    private Long organizerId;
    private Long chatId;
    private Set<UserEventDto> participateUsers;
}
