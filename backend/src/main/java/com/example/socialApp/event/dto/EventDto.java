package com.example.socialApp.event.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class EventDto {

    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 3, max = 50, message = "Name must contain between 3 and 50 characters.")
    private String name;

    @NotBlank(message = "Description cannot be blank.")
    @Size(min = 1, max = 300, message = "Description must contain between 1 and 300 characters.")
    private String description;

    @Min(value = 12, message = "Age limit must be more than 11")
    @Max(value = 18, message = "Age limit must be more than 19")
    private Integer ageLimit;

    @Min(value = 2, message = "Max number of participant must be more than 1")
    @Max(value = 149, message = "Max number of participant must be less than 150")
    private Integer maxNumberOfParticipant;

    @NotBlank(message = "Location cannot be blank.")
    @Size(min = 1, max = 200, message = "Location must contain between 1 and 200 characters.")
    private String location;

    @NotNull(message = "Date cannot be null.")
    private LocalDateTime date;

    private Boolean freeJoin;

    private Set<String> categories;

    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 3, max = 50, message = "Name must contain between 3 and 50 characters.")
    private String chatName;
}
