package com.example.socialApp.event.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class EventDateDto {

    @NotNull(message = "Date cannot be null.")
    private LocalDateTime date;
}
