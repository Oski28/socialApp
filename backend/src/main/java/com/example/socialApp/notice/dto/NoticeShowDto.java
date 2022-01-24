package com.example.socialApp.notice.dto;

import lombok.Data;

@Data
public class NoticeShowDto {

    private Long id;
    private String content;
    private Boolean received;
}
