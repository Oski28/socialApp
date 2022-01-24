package com.example.socialApp.event.dto;

import lombok.Data;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.validation.constraints.*;

@Data
public class EventUpdateDto {

    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 3, max = 50, message = "Name must contain between 3 and 50 characters.")
    private String name;

    @NotBlank(message = "Description cannot be blank.")
    @Size(min = 1, max = 300, message = "Description must contain between 1 and 300 characters.")
    private String description;

    @Min(value = 12, message = "Age limit must be more than 11")
    @Max(value = 18, message = "Age limit must be more than 19")
    private Integer ageLimit;

    @NotBlank(message = "Location cannot be blank.")
    @Size(min = 1, max = 200, message = "Location must contain between 1 and 200 characters.")
    private String location;

    @Generated(GenerationTime.INSERT)
    private Boolean freeJoin;
}
