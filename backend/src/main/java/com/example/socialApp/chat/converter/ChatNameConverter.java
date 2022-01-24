package com.example.socialApp.chat.converter;

import com.example.socialApp.chat.dto.ChatNameDto;
import com.example.socialApp.chat.model_repo.Chat;
import com.example.socialApp.shared.BaseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ChatNameConverter extends BaseConverter<Chat, ChatNameDto> {

    @Override
    public Function<ChatNameDto, Chat> toEntity() {
        return null;
    }

    @Override
    public Function<Chat, ChatNameDto> toDto() {
        return chat -> {
            if (chat == null)
                return null;

            ChatNameDto dto = new ChatNameDto();
            dto.setId(chat.getId());
            dto.setName(chat.getName());
            if (!chat.getMessages().isEmpty()) {
                dto.setLastMessage(chat.getMessages().get(chat.getMessages().size() - 1).getWriteDate());
            }
            return dto;
        };
    }
}
