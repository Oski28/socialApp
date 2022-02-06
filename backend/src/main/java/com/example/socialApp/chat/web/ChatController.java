package com.example.socialApp.chat.web;

import com.example.socialApp.chat.converter.ChatConverter;
import com.example.socialApp.chat.converter.ChatNameConverter;
import com.example.socialApp.chat.converter.ChatShowConverter;
import com.example.socialApp.chat.converter.ChatUpdateConverter;
import com.example.socialApp.chat.dto.ChatDto;
import com.example.socialApp.chat.dto.ChatNameDto;
import com.example.socialApp.chat.dto.ChatShowDto;
import com.example.socialApp.chat.dto.ChatUpdateDto;
import com.example.socialApp.chat.model_repo.Chat;
import com.example.socialApp.exceptions.OperationAccessDeniedException;
import com.example.socialApp.shared.BaseController;
import com.example.socialApp.shared.BaseService;
import com.example.socialApp.user.web.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/chats")
public class ChatController extends BaseController<Chat> {

    private final ChatServiceImplementation chatService;

    private final ChatNameConverter chatNameConverter;

    private final ChatShowConverter chatShowConverter;

    private final ChatUpdateConverter chatUpdateConverter;

    private final ChatConverter chatConverter;

    private final UserServiceImplementation userService;

    @Autowired
    public ChatController(BaseService<Chat> service, ChatServiceImplementation chatService,
                          ChatNameConverter chatNameConverter, ChatShowConverter chatShowConverter,
                          ChatUpdateConverter chatUpdateConverter, ChatConverter chatConverter,
                          UserServiceImplementation userService) {
        super(service);
        this.chatService = chatService;
        this.chatNameConverter = chatNameConverter;
        this.chatShowConverter = chatShowConverter;
        this.chatUpdateConverter = chatUpdateConverter;
        this.chatConverter = chatConverter;
        this.userService = userService;
    }

    /* GET */

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ChatShowDto> getOne(@PathVariable final Long id) {
        Chat chat = this.chatService.getById(id);
        if (this.chatService.checkIfUserIsChatParticipant(chat))
            return super.getOne(id, this.chatShowConverter.toDto());
       else throw new OperationAccessDeniedException("Brak uprawnień do pobrania danych czatu o id " + id);
    }

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<ChatNameDto>> getAllForUser(@RequestParam(defaultValue = "0") final int page,
                                                           @RequestParam(defaultValue = "20") final int size,
                                                           @RequestParam(defaultValue = "id") final String column,
                                                           @RequestParam(defaultValue = "ASC") final String direction,
                                                           @RequestParam(defaultValue = "") final String filter) {
        Sort.Direction sortDir = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Page<Chat> chats;
        if (filter.equals("")) {
            chats = this.chatService.getAllForUser(page, size, column, sortDir);
        } else {
            chats = this.chatService.getAllForUserWithFilter(page, size, column, sortDir, filter);
        }
        return ResponseEntity.ok(chats.map(this.chatNameConverter.toDto()));
    }

    /* POST */
    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@RequestBody @Valid ChatDto dto) {
        Chat chat = this.chatConverter.toEntity().apply(dto);
        Long id = chatService.isExistsByUsers(chat);
        if (id != null) {
            return ResponseEntity.ok(id);
        }
        Chat savedChat = chatService.save(chat);
        this.userService.addChatToUser(savedChat.getUsers().get(0).getId(), savedChat);
        this.userService.addChatToUser(savedChat.getUsers().get(1).getId(), savedChat);
        return ResponseEntity.ok(savedChat.getId());
    }

    /* PUT */

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> update(@PathVariable final Long id, @RequestBody @Valid final ChatUpdateDto dto) {
        Chat chat = this.chatService.getById(id);
        if (this.chatService.checkIfUserIsChatParticipant(chat))
            return super.update(id, this.chatUpdateConverter.toEntity().apply(dto));
        else throw new OperationAccessDeniedException("Brak uprawnień do edytowania danych czatu o id " + id);
    }
}
