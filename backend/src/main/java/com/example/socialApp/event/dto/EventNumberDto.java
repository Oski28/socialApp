package com.example.socialApp.event.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class EventNumberDto {

    @Min(value = 2, message = "Max number of participant must be more than 1")
    @Max(value = 149, message = "Max number of participant must be less than 150")
    private Integer maxNumberOfParticipant;
}
