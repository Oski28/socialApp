package com.example.socialApp.message.web;

import com.example.socialApp.chat.model_repo.Chat;
import com.example.socialApp.message.dto.MessageDto;
import com.example.socialApp.message.dto.MessageFileDto;
import com.example.socialApp.message.dto.MessageShowDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.example.socialApp.message.model_repo.Message;

public interface MessageService {

    void removeMessagesForDeletedChat(Chat chat);

    Page<Message> getAllForChat(int page, int size, String column, Sort.Direction sortDir, Long chatId);

    Message save(Message entity, Long id, Long userId);

    MessageShowDto saveAndReturnShowDto(MessageDto dto, Long id);

    MessageFileDto getFile(Long id);
}
