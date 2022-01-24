package com.example.socialApp.category.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CategoryShowDto {

    private Long id;
    private String name;
    private Set<String> eventsName;
}
