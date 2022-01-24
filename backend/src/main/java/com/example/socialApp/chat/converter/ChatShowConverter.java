package com.example.socialApp.chat.converter;

import com.example.socialApp.chat.dto.ChatShowDto;
import com.example.socialApp.chat.model_repo.Chat;
import com.example.socialApp.shared.BaseConverter;
import com.example.socialApp.user.model_repo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatShowConverter extends BaseConverter<Chat, ChatShowDto> {

    @Override
    public Function<ChatShowDto, Chat> toEntity() {
        return null;
    }

    @Override
    public Function<Chat, ChatShowDto> toDto() {
        return chat -> {
            if (chat == null)
                return null;

            ChatShowDto dto = new ChatShowDto();
            dto.setId(chat.getId());
            dto.setName(chat.getName());
            if (chat.getEvent() == null) {
                dto.setEventName("Czat indywidualny");
            } else {
                dto.setEventName(chat.getEvent().getName());
            }
            dto.setUsersName(chat.getUsers().stream().map(User::getUsername).collect(Collectors.toSet()));

            return dto;
        };
    }
}
