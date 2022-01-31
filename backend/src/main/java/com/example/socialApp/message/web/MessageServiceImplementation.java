package com.example.socialApp.message.web;

import com.example.socialApp.chat.model_repo.Chat;
import com.example.socialApp.chat.web.ChatServiceImplementation;
import com.example.socialApp.exceptions.OperationAccessDeniedException;
import com.example.socialApp.message.converter.MessageConverter;
import com.example.socialApp.message.converter.MessageShowConverter;
import com.example.socialApp.message.dto.MessageDto;
import com.example.socialApp.message.dto.MessageFileDto;
import com.example.socialApp.message.dto.MessageShowDto;
import com.example.socialApp.message.model_repo.Message;
import com.example.socialApp.message.model_repo.MessageRepository;
import com.example.socialApp.shared.BaseService;
import com.example.socialApp.user.model_repo.User;
import com.example.socialApp.user.web.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.DatatypeConverter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImplementation implements MessageService, BaseService<Message> {

    private final MessageRepository messageRepository;

    private ChatServiceImplementation chatService;

    private UserServiceImplementation userService;

    private MessageConverter messageConverter;

    private MessageShowConverter messageShowConverter;

    @Autowired
    public void setUserService(UserServiceImplementation userService) {
        this.userService = userService;
    }

    @Autowired
    public void setChatService(ChatServiceImplementation chatService) {
        this.chatService = chatService;
    }

    @Autowired
    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Autowired
    public void setMessageShowConverter(MessageShowConverter messageShowConverter) {
        this.messageShowConverter = messageShowConverter;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void removeMessagesForDeletedChat(Chat chat) {
        List<Message> messages = this.messageRepository.findAllByChat(chat);
        for (Message message : messages) {
            this.messageRepository.delete(message);
            chat.getMessages().remove(message);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public Page<Message> getAllForChat(int page, int size, String column, Sort.Direction sortDir, Long chatId) {
        if (this.chatService.checkIfUserIsChatParticipant(this.chatService.getById(chatId))) {
            Sort sort = Sort.by(sortDir, column, "id");
            Chat chat = this.chatService.getById(chatId);
            return this.messageRepository.getAllByChat(chat, PageRequest.of(page, size, sort));
        } else throw new OperationAccessDeniedException("Brak uprawnień do pobrania danych czatu o id " + chatId);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<Message> getAll(int page, int size, String column, Sort.Direction direction) {
        Sort sort = Sort.by(new Sort.Order(direction, column));
        return this.messageRepository.findAll(PageRequest.of(page, size, sort));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean update(Long id, Message entity) {
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean delete(Long id) {
        Optional<Message> optionalMessage = findById(id);
        if (optionalMessage.isPresent()) {
            User user = this.userService.getAuthUser();
            Message message = optionalMessage.get();

            if (!message.getUser().equals(user)) {
                throw new OperationAccessDeniedException("Brak uprawnień do usunięcia wiadomości");
            }
            message.setDeleteDate(LocalDateTime.now());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Message save(Message entity) {
        return this.messageRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Message> findById(Long id) {
        return this.messageRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Message getById(Long id) {
        return this.messageRepository.getById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public Message save(Message entity, Long id, Long userId) {
        Optional<Chat> optionalChat = this.chatService.findById(id);
        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get();
            User user = userService.getAuthUser();
            if (user == null) {
                user = userService.getById(userId);
            }
            entity.setChat(chat);
            entity.setUser(user);
            return messageRepository.save(entity);
        } else throw new EntityNotFoundException("Brak chatu o podanym id " + id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public MessageShowDto saveAndReturnShowDto(MessageDto dto, Long id) {
        return messageShowConverter.toDto().apply(save(messageConverter.toEntity().apply(dto), id, dto.getUserId()));
    }

    @Override
    @Transactional(readOnly = true)
    public MessageFileDto getFile(Long id) {
        Message message = getById(id);
        if (!this.chatService.checkIfUserIsChatParticipant(message.getChat())) {
            throw new OperationAccessDeniedException("Nie jesteś uczestnikiem czatu. Pobranie załącznika niemożliwe.");
        }
        if (message == null || message.getFile() == null) {
            throw new NullPointerException("Brak takiego załącznika.");
        }
        MessageFileDto dto = new MessageFileDto();
        dto.setFileName(message.getFileName());
        dto.setFileType(message.getFile()
                .substring(message.getFile().indexOf(":") + 1, message.getFile().indexOf(";") + 1));
        dto.setFileData(DatatypeConverter.parseBase64Binary(message
                .getFile().substring(message.getFile().indexOf(",") + 1)));
        return dto;
    }
}
