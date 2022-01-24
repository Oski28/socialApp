package com.example.socialApp.requestToJoin.converter;

import com.example.socialApp.requestToJoin.dto.RequestToJoinUpdateDto;
import com.example.socialApp.requestToJoin.model_repo.RequestToJoin;
import com.example.socialApp.requestToJoin.model_repo.Status;
import com.example.socialApp.shared.BaseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class RequestToJoinUpdateConverter extends BaseConverter<RequestToJoin, RequestToJoinUpdateDto> {


    @Override
    public Function<RequestToJoinUpdateDto, RequestToJoin> toEntity() {
        return dto -> {
            if (dto == null)
                return null;

            RequestToJoin requestToJoin = new RequestToJoin();
            switch (dto.getStatus().toUpperCase()) {
                case "REJECTED":
                    requestToJoin.setStatus(Status.REJECTED);
                    break;
                case "ACCEPTED":
                    requestToJoin.setStatus(Status.ACCEPTED);
                    break;
                case "WAITING":
                default:
                    requestToJoin.setStatus(Status.WAITING);
                    break;
            }
            requestToJoin.setAddDate(LocalDateTime.now());
            return requestToJoin;
        };
    }

    @Override
    public Function<RequestToJoin, RequestToJoinUpdateDto> toDto() {
        return null;
    }
}
