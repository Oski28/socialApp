package com.example.socialApp.event.converter;

import com.example.socialApp.event.dto.EventNumberDto;
import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.shared.BaseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class EventNumberConverter extends BaseConverter<Event, EventNumberDto> {
    @Override
    public Function<EventNumberDto, Event> toEntity() {
        return dto -> {
            Event event = new Event();
            convertIfNotNull(event::setMaxNumberOfParticipant, dto::getMaxNumberOfParticipant);
            return event;
        };
    }

    @Override
    public Function<Event, EventNumberDto> toDto() {
        return event -> {
            EventNumberDto dto = new EventNumberDto();
            convertIfNotNull(dto::setMaxNumberOfParticipant, event::getMaxNumberOfParticipant);
            return dto;
        };
    }
}
