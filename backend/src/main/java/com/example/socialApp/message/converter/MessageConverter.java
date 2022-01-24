package com.example.socialApp.message.converter;

import com.example.socialApp.message.dto.MessageDto;
import com.example.socialApp.message.model_repo.Message;
import com.example.socialApp.shared.BaseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class MessageConverter extends BaseConverter<Message, MessageDto> {

    @Override
    public Function<MessageDto, Message> toEntity() {
        return dto -> {

            if (dto == null)
                return null;

            Message message = new Message();
            message.setContent(dto.getContent());
            message.setWriteDate(LocalDateTime.now());
            message.setFile(dto.getFile());
            message.setFileName(dto.getFileName());

            return message;
        };
    }

    @Override
    public Function<Message, MessageDto> toDto() {
        return null;
    }
}
