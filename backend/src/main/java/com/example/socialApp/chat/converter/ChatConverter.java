package com.example.socialApp.chat.converter;

import com.example.socialApp.chat.dto.ChatDto;
import com.example.socialApp.chat.model_repo.Chat;
import com.example.socialApp.exceptions.DuplicateObjectException;
import com.example.socialApp.exceptions.OperationAccessDeniedException;
import com.example.socialApp.shared.BaseConverter;
import com.example.socialApp.user.model_repo.User;
import com.example.socialApp.user.web.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ChatConverter extends BaseConverter<Chat, ChatDto> {

    private final UserServiceImplementation userService;

    @Override
    public Function<ChatDto, Chat> toEntity() {
        return dto -> {
            if (dto == null)
                return null;

            Chat chat = new Chat();
            User creator = this.userService.getAuthUser();
            Optional<User> optional = this.userService.findById(dto.getUserId());
            User second = optional.orElseThrow(() -> new NoSuchElementException("Użytkownik z którym chcesz utworzyć" +
                    "czat nie istnieje"));
            if (creator.equals(second)) {
                throw new DuplicateObjectException("Nie można utworzyć czatu z samym sobą!");
            }
            if (!second.getEnabled()) {
                throw new OperationAccessDeniedException("Nie można stworzyć czatu z nieakatywnym użytkownikiem");
            }
            chat.setUsers(List.of(creator, second));
            chat.setName(creator.getUsername() + " - " + second.getUsername());

            return chat;
        };
    }

    @Override
    public Function<Chat, ChatDto> toDto() {
        return null;
    }
}
