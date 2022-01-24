package com.example.socialApp.event.converter;

import com.example.socialApp.event.dto.EventUpdateDto;
import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.shared.BaseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class EventUpdateConverter extends BaseConverter<Event, EventUpdateDto> {
    @Override
    public Function<EventUpdateDto, Event> toEntity() {
        return dto -> {
            if (dto == null)
                return null;

            Event event = new Event();

            event.setName(dto.getName());
            event.setDescription(dto.getDescription());
            event.setLocation(dto.getLocation());
            event.setFreeJoin(dto.getFreeJoin());
            event.setAgeLimit(dto.getAgeLimit());

            return event;
        };
    }

    @Override
    public Function<Event, EventUpdateDto> toDto() {
        return event -> {
            if (event == null)
                return null;

            EventUpdateDto dto = new EventUpdateDto();

            dto.setAgeLimit(event.getAgeLimit());
            dto.setDescription(event.getDescription());
            dto.setFreeJoin(event.getFreeJoin());
            dto.setLocation(event.getLocation());
            dto.setName(event.getName());

            return dto;
        };
    }
}
