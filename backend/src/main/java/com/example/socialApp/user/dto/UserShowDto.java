package com.example.socialApp.user.dto;

import com.example.socialApp.chat.dto.ChatNameDto;
import com.example.socialApp.event.dto.EventShowDto;
import com.example.socialApp.role.dto.RoleShowDto;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class UserShowDto {

    private Long id;
    private String avatar;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private LocalDate dateOfBirth;
    private LocalDateTime addDate;
    private Boolean blocked;
    private Boolean enabled;
    private LocalDateTime banExpirationDate;
    private Set<RoleShowDto> roles;
    private Set<ChatNameDto> chats;
    private List<EventShowDto> userCreatedEvents;
    private List<EventShowDto> userParticipatedEvents;
}
