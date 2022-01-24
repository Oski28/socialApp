package com.example.socialApp.event.dto;

import lombok.Data;

import java.util.Set;

@Data
public class EventCategoryDto {

    private Set<String> categories;
}
