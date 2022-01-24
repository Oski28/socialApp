package com.example.socialApp.category.converter;

import com.example.socialApp.category.dto.CategoryShowDto;
import com.example.socialApp.category.model_repo.Category;
import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.shared.BaseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryShowConverter extends BaseConverter<Category, CategoryShowDto> {


    @Override
    public Function<CategoryShowDto, Category> toEntity() {
        return null;
    }

    @Override
    public Function<Category, CategoryShowDto> toDto() {
        return category -> {
            if (category == null)
                return null;

            CategoryShowDto dto = new CategoryShowDto();
            dto.setId(category.getId());
            dto.setName(category.getName());
            dto.setEventsName(category.getEvents().stream().map(Event::getName).collect(Collectors.toSet()));
            return dto;
        };
    }
}
