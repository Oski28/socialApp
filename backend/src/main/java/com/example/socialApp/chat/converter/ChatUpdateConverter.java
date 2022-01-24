package com.example.socialApp.chat.converter;

import com.example.socialApp.chat.dto.ChatUpdateDto;
import com.example.socialApp.chat.model_repo.Chat;
import com.example.socialApp.shared.BaseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ChatUpdateConverter extends BaseConverter<Chat, ChatUpdateDto> {
    @Override
    public Function<ChatUpdateDto, Chat> toEntity() {
        return dto -> {
            if (dto == null)
                return null;

            Chat chat = new Chat();
            chat.setName(dto.getName());

            return chat;
        };
    }

    @Override
    public Function<Chat, ChatUpdateDto> toDto() {
        return chat -> {
            if (chat == null)
                return null;

            ChatUpdateDto dto = new ChatUpdateDto();
            dto.setName(chat.getName());
            return dto;
        };
    }
}
