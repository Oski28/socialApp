package com.example.socialApp.report.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ReportDto {

    @NotBlank(message = "Reason cannot be blank.")
    @Size(min = 1, max = 300, message = "Reason must contain between 1 and 300 characters.")
    private String reason;
    private Long userId;
    private Long eventId;
}
