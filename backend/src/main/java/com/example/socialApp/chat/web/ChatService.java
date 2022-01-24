package com.example.socialApp.chat.web;

import com.example.socialApp.chat.model_repo.Chat;
import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.user.model_repo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface ChatService {

    void prepareChatToRemoveForDeletedEvent(Event event);

    void removeUserForAllChats(User user);

    Page<Chat> getAllForUserWithFilter(int page, int size, String column, Sort.Direction direction, String filter);

    Page<Chat> getAllForUser(int page, int size, String column, Sort.Direction direction);

    Boolean checkIfUserIsChatParticipant(Chat chat);

    Page<Chat> sortByLastMessage(Page<Chat> chats);

    Long isExistsByUsers(Chat chat);
}
