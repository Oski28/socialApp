package com.example.socialApp.user.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserEventDto {
    private Long id;
    private String avatar;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private LocalDate dateOfBirth;
    private LocalDateTime addDate;
    private Boolean blocked;
}
