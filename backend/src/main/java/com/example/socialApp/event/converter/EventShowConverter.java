package com.example.socialApp.event.converter;


import com.example.socialApp.category.model_repo.Category;
import com.example.socialApp.event.dto.EventShowDto;
import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.shared.BaseConverter;
import com.example.socialApp.user.converter.UserEventConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventShowConverter extends BaseConverter<Event, EventShowDto> {

    private final UserEventConverter userEventConverter;

    @Override
    public Function<EventShowDto, Event> toEntity() {
        return null;
    }

    @Override
    public Function<Event, EventShowDto> toDto() {
        return event -> {
            if (event == null)
                return null;

            EventShowDto dto = new EventShowDto();
            dto.setId(event.getId());
            dto.setAgeLimit(event.getAgeLimit());
            dto.setName(event.getName());
            dto.setDate(event.getDate());
            dto.setDescription(event.getDescription());
            dto.setFreeJoin(event.getFreeJoin());
            dto.setLocation(event.getLocation());
            dto.setMaxNumberOfParticipant(event.getMaxNumberOfParticipant());
            dto.setCategories(event.getCategories().stream().map(Category::getName).collect(Collectors.toSet()));
            dto.setOrganizer(event.getUser().getFirstname() + " " + event.getUser().getLastname()
                    + "(" + event.getUser().getUsername() + ")");
            dto.setParticipateUsers(event.getUsers().stream()
                    .map(this.userEventConverter.toDto()).collect(Collectors.toSet()));
            dto.setOrganizerId(event.getUser().getId());
            dto.setChatId(event.getChat().getId());

            return dto;
        };
    }
}
