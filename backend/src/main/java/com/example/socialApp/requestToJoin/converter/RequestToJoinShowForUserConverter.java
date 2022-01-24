package com.example.socialApp.requestToJoin.converter;

import com.example.socialApp.requestToJoin.dto.RequestToJoinShowForUserDto;
import com.example.socialApp.requestToJoin.model_repo.RequestToJoin;
import com.example.socialApp.shared.BaseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class RequestToJoinShowForUserConverter extends BaseConverter<RequestToJoin, RequestToJoinShowForUserDto> {


    @Override
    public Function<RequestToJoinShowForUserDto, RequestToJoin> toEntity() {
        return null;
    }

    @Override
    public Function<RequestToJoin, RequestToJoinShowForUserDto> toDto() {
        return requestToJoin -> {
            if (requestToJoin == null)
                return null;

            RequestToJoinShowForUserDto dto = new RequestToJoinShowForUserDto();

            dto.setId(requestToJoin.getId());
            dto.setDate(requestToJoin.getEvent().getDate());
            dto.setEventId(requestToJoin.getEvent().getId());
            dto.setEventName(requestToJoin.getEvent().getName());
            dto.setStatus(requestToJoin.getStatus());
            dto.setAddDate(requestToJoin.getAddDate());

            return dto;
        };
    }
}
