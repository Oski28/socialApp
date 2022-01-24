package com.example.socialApp.event.converter;

import com.example.socialApp.category.model_repo.Category;
import com.example.socialApp.category.model_repo.CategoryRepository;
import com.example.socialApp.event.dto.EventCategoryDto;
import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.shared.BaseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventCategoryConverter extends BaseConverter<Event, EventCategoryDto> {

    private final CategoryRepository categoryRepository;

    private static final String DEFAULT_CATEGORY = "Inne";

    @Override
    public Function<EventCategoryDto, Event> toEntity() {
        return dto -> {
            if (dto == null)
                return null;

            Event event = new Event();

            Set<String> strCategories = dto.getCategories();
            Set<Category> categories = new HashSet<>();

            if (strCategories.isEmpty()) {
                Category category = this.categoryRepository.findByName(DEFAULT_CATEGORY)
                        .orElseThrow(() -> new RuntimeException("Error: Category is not found"));
                categories.add(category);
            } else {
                strCategories.forEach(s -> {
                    Category category = this.categoryRepository.findByName(s)
                            .orElseThrow(() -> new RuntimeException("Error: Category is not found"));
                    categories.add(category);
                });
            }
            event.setCategories(categories);

            return event;
        };
    }

    @Override
    public Function<Event, EventCategoryDto> toDto() {
        return event -> {
            if (event == null)
                return null;

            EventCategoryDto dto = new EventCategoryDto();

            dto.setCategories(event.getCategories().stream().map(Category::getName).collect(Collectors.toSet()));

            return dto;
        };
    }
}
