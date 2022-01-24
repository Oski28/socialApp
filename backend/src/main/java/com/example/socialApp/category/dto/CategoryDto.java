package com.example.socialApp.category.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CategoryDto {

    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 1, max = 100, message = "Description must contain between 1 and 100 characters.")
    private String name;
}
