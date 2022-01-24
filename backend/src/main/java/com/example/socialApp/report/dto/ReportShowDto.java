package com.example.socialApp.report.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportShowDto {

    private Long id;
    private LocalDateTime addDate;
    private String reason;
    private Boolean received;
    private Long userId;
    private Long eventId;
}
