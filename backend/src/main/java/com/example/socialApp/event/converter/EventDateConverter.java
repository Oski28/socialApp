package com.example.socialApp.event.converter;

import com.example.socialApp.event.dto.EventDateDto;
import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.shared.BaseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class EventDateConverter extends BaseConverter<Event, EventDateDto> {

    @Override
    public Function<EventDateDto, Event> toEntity() {
        return dto -> {
            Event event = new Event();
            convertIfNotNull(event::setDate, dto::getDate);
            return event;
        };
    }

    @Override
    public Function<Event, EventDateDto> toDto() {
        return event -> {
            EventDateDto dto = new EventDateDto();
            convertIfNotNull(dto::setDate, event::getDate);
            return dto;
        };
    }
}
