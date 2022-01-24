package com.example.socialApp.event.converter;

import com.example.socialApp.category.model_repo.Category;
import com.example.socialApp.category.model_repo.CategoryRepository;
import com.example.socialApp.event.dto.EventDto;
import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.shared.BaseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class EventConverter extends BaseConverter<Event, EventDto> {

    private static final String DEFAULT_CATEGORY = "Inne";

    private final CategoryRepository categoryRepository;

    @Override
    public Function<EventDto, Event> toEntity() {
        return dto -> {
            if (dto == null)
                return null;

            Event event = new Event();
            event.setName(dto.getName());
            event.setDescription(dto.getDescription());
            event.setAgeLimit(dto.getAgeLimit());
            event.setMaxNumberOfParticipant(dto.getMaxNumberOfParticipant());
            event.setDate(dto.getDate());
            event.setFreeJoin(dto.getFreeJoin());
            event.setLocation(dto.getLocation());

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
    public Function<Event, EventDto> toDto() {
        return null;
    }
}
