package com.example.socialApp.requestToJoin.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RequestToJoinUpdateDto {

    @NotBlank(message = "Status cannot be blank.")
    @Size(min = 7, max = 8, message = "Status must contain between 7 and 8 characters.")
    private String status;
}
