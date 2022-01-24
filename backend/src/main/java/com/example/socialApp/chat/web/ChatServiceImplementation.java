package com.example.socialApp.chat.web;

import com.example.socialApp.chat.converter.ChatComparator;
import com.example.socialApp.chat.model_repo.Chat;
import com.example.socialApp.chat.model_repo.ChatRepository;
import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.message.web.MessageServiceImplementation;
import com.example.socialApp.shared.BaseService;
import com.example.socialApp.user.model_repo.User;
import com.example.socialApp.user.web.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImplementation implements ChatService, BaseService<Chat> {

    private final ChatRepository chatRepository;

    private MessageServiceImplementation messageService;

    private UserServiceImplementation userService;

    private ChatComparator chatComparator;

    @Autowired
    public void setMessageService(MessageServiceImplementation messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setUserService(UserServiceImplementation userService) {
        this.userService = userService;
    }

    @Autowired
    public void setChatComparator(ChatComparator chatComparator) {
        this.chatComparator = chatComparator;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Chat> getAll(int page, int size, String column, Sort.Direction direction) {
        Sort sort = Sort.by(new Sort.Order(direction, column));
        return this.chatRepository.findAll(PageRequest.of(page, size, sort));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean update(Long id, Chat entity) {
        if (isExists(id)) {
            Chat chat = getById(id);
            chat.setName(entity.getName());
            return true;
        } else return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean delete(Long id) {
        if (isExists(id)) {
            this.chatRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Chat save(Chat entity) {
        return this.chatRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Chat getById(Long id) {
        if (isExists(id)) {
            return this.chatRepository.getById(id);
        } else {
            return null;
        }

    }

    @Override
    public boolean isExists(Long id) {
        return chatRepository.existsById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void prepareChatToRemoveForDeletedEvent(Event event) {
        Chat deleteChat = getById(event.getChat().getId());
        List<User> chatUsers = deleteChat.getUsers();
        for (User chatUser : chatUsers) {
            chatUser.getChats().remove(deleteChat);
        }
        List<User> users = deleteChat.getUsers();
        Iterator<User> userIterator = users.iterator();
        while (userIterator.hasNext()) {
            userIterator.next();
            userIterator.remove();
        }
        this.messageService.removeMessagesForDeletedChat(deleteChat);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void removeUserForAllChats(User user) {
        List<Chat> chats = this.chatRepository.findAll();
        for (Chat chat : chats) {
            user.getChats().remove(chat);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public Page<Chat> getAllForUserWithFilter(int page, int size, String column, Sort.Direction direction, String filter) {
        User user = this.userService.getAuthUser();
        Sort sort = Sort.by(new Sort.Order(direction, column));
        return sortByLastMessage(this.chatRepository.findAllByNameContainsAndUsersContaining(filter, user, PageRequest.of(page, size, sort)));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public Page<Chat> getAllForUser(int page, int size, String column, Sort.Direction direction) {
        User user = this.userService.getAuthUser();
        Sort sort = Sort.by(new Sort.Order(direction, column));
        return sortByLastMessage(this.chatRepository.findAllByUsersContaining(user, PageRequest.of(page, size, sort)));
    }

    @Override
    public Boolean checkIfUserIsChatParticipant(Chat chat) {
        User user = this.userService.getAuthUser();
        return chat.getUsers().contains(user);
    }

    @Override
    public Page<Chat> sortByLastMessage(Page<Chat> chats) {
        return new PageImpl<>(chats.stream().sorted((c1, c2) -> chatComparator.compare(c1, c2)).collect(Collectors.toList()));
    }

    @Override
    @Transactional(readOnly = true)
    public Long isExistsByUsers(Chat chat) {
        Chat dbChat = chatRepository.getByUsersContainingAndUsersContainingAndEventIsNull(chat.getUsers().get(0), chat.getUsers().get(1));
        if (dbChat == null) {
            return null;
        } else return dbChat.getId();
    }
}
