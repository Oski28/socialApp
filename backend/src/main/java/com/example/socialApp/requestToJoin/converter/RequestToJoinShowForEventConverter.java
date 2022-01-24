package com.example.socialApp.requestToJoin.converter;

import com.example.socialApp.requestToJoin.dto.RequestToJoinShowForEventDto;
import com.example.socialApp.requestToJoin.model_repo.RequestToJoin;
import com.example.socialApp.shared.BaseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class RequestToJoinShowForEventConverter extends BaseConverter<RequestToJoin, RequestToJoinShowForEventDto> {
    @Override
    public Function<RequestToJoinShowForEventDto, RequestToJoin> toEntity() {
        return null;
    }

    @Override
    public Function<RequestToJoin, RequestToJoinShowForEventDto> toDto() {
        return requestToJoin -> {
            if (requestToJoin == null)
                return null;

            RequestToJoinShowForEventDto dto = new RequestToJoinShowForEventDto();
            dto.setId(requestToJoin.getId());
            dto.setAvatar(requestToJoin.getUser().getAvatar());
            dto.setUsername(requestToJoin.getUser().getUsername());
            dto.setStatus(requestToJoin.getStatus());
            dto.setUserId(requestToJoin.getUser().getId());
            dto.setAddDate(requestToJoin.getAddDate());
            return dto;
        };
    }
}
