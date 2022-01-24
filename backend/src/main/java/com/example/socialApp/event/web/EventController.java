package com.example.socialApp.event.web;

import com.example.socialApp.chat.model_repo.Chat;
import com.example.socialApp.chat.web.ChatServiceImplementation;
import com.example.socialApp.event.converter.*;
import com.example.socialApp.event.dto.*;
import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.exceptions.AgeLimitException;
import com.example.socialApp.exceptions.DuplicateObjectException;
import com.example.socialApp.shared.BaseController;
import com.example.socialApp.shared.BaseService;
import com.example.socialApp.user.model_repo.User;
import com.example.socialApp.user.web.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/events")
@CrossOrigin
public class EventController extends BaseController<Event> {

    private final EventServiceImplementation eventService;

    private final EventShowConverter eventShowConverter;

    private final EventConverter eventConverter;

    private final UserServiceImplementation userService;

    private final ChatServiceImplementation chatService;

    private final EventUpdateConverter eventUpdateConverter;

    private final EventDateConverter eventDateConverter;

    private final EventNumberConverter eventNumberConverter;

    private final EventCategoryConverter eventCategoryConverter;

    @Autowired
    public EventController(BaseService<Event> service, EventServiceImplementation eventService,
                           EventShowConverter eventShowConverter, EventConverter eventConverter,
                           UserServiceImplementation userService, ChatServiceImplementation chatService,
                           EventUpdateConverter eventUpdateConverter, EventDateConverter eventDateConverter,
                           EventNumberConverter eventNumberConverter, EventCategoryConverter eventCategoryConverter) {
        super(service);
        this.eventService = eventService;
        this.eventShowConverter = eventShowConverter;
        this.eventConverter = eventConverter;
        this.userService = userService;
        this.chatService = chatService;
        this.eventUpdateConverter = eventUpdateConverter;
        this.eventDateConverter = eventDateConverter;
        this.eventNumberConverter = eventNumberConverter;
        this.eventCategoryConverter = eventCategoryConverter;
    }

    /* GET */

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EventShowDto> getOne(@PathVariable final Long id) {
        return super.getOne(id, this.eventShowConverter.toDto());
    }

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<EventShowDto>> getAll(@RequestParam(defaultValue = "0") final int page,
                                                     @RequestParam(defaultValue = "20") final int size,
                                                     @RequestParam(defaultValue = "id") final String column,
                                                     @RequestParam(defaultValue = "ASC") final String direction,
                                                     @RequestParam(defaultValue = "") final String filter,
                                                     @RequestParam(defaultValue = "true") final boolean activeDate) {
        Sort.Direction sortDir = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(this.eventService.getAll(page, size, column, sortDir, filter, activeDate).map(this.eventShowConverter.toDto()));
    }

    @GetMapping("/user/nonparticipating")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<EventShowDto>> getAllForAuthUserNonParticipate(@RequestParam(defaultValue = "0") final int page,
                                                                              @RequestParam(defaultValue = "20") final int size,
                                                                              @RequestParam(defaultValue = "id") final String column,
                                                                              @RequestParam(defaultValue = "ASC") final String direction,
                                                                              @RequestParam(defaultValue = "") final String filter,
                                                                              @RequestParam(defaultValue = "true") final boolean activeDate) {
        Sort.Direction sortDir = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(this.eventService.getAllForAuthUserNonParticipate(page, size, column, sortDir, filter, activeDate).map(this.eventShowConverter.toDto()));
    }

    @GetMapping("/user/participate")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<EventShowDto>> getAllForAuthUserParticipate(@RequestParam(defaultValue = "0") final int page,
                                                                           @RequestParam(defaultValue = "20") final int size,
                                                                           @RequestParam(defaultValue = "id") final String column,
                                                                           @RequestParam(defaultValue = "ASC") final String direction,
                                                                           @RequestParam(defaultValue = "") final String filter,
                                                                           @RequestParam(defaultValue = "true") final boolean activeDate) {
        Sort.Direction sortDir = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(this.eventService.getAllForUserParticipate(page, size, column, sortDir, filter, activeDate)
                .map(this.eventShowConverter.toDto()));
    }

    @GetMapping("/user/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<EventShowDto>> getAllForAuthUserCreate(@RequestParam(defaultValue = "0") final int page,
                                                                      @RequestParam(defaultValue = "20") final int size,
                                                                      @RequestParam(defaultValue = "id") final String column,
                                                                      @RequestParam(defaultValue = "ASC") final String direction,
                                                                      @RequestParam(defaultValue = "") final String filter,
                                                                      @RequestParam(defaultValue = "true") final boolean activeDate) {
        Sort.Direction sortDir = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(this.eventService.getAllForUserCreate(page, size, column, sortDir, filter, activeDate)
                .map(this.eventShowConverter.toDto()));
    }

    /* POST */

    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@RequestBody @Valid final EventDto dto) {
        if (eventService.existByName(dto.getName())) {
            throw new DuplicateObjectException("Wydarzenie o nazwie " + dto.getName() + " istnieje w aplikacji");
        }

        Event newEvent = this.eventConverter.toEntity().apply(dto);

        User user = this.userService.getAuthUser();
        if (dto.getAgeLimit() != null) {
            if (this.userService.calculateAge(user.getDateOfBirth()) < dto.getAgeLimit()) {
                throw new AgeLimitException("Limit wieku dla wydarzenia nie może być wyższy niż" +
                        " wiek użytkownika tworzącego wydarzenie.");
            }
        }

        newEvent.setUser(user);
        newEvent.setUsers(Set.of(user));

        Chat chat = new Chat();
        chat.setName(dto.getChatName());

        Chat savedChat = this.chatService.save(chat);
        newEvent.setChat(savedChat);

        Event savedEvent = this.eventService.save(newEvent);

        this.userService.addChatToUser(user.getId(), savedChat);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedEvent.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    /* PUT */

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> update(@PathVariable final Long id, @RequestBody @Valid final EventUpdateDto dto) {
        if (eventService.existByName(dto.getName()) && !this.eventService.getById(id).getName().equals(dto.getName())) {
            throw new DuplicateObjectException("Wydarzenie o nazwie " + dto.getName() + " istnieje w aplikacji");
        }
        return super.update(id, this.eventUpdateConverter.toEntity().apply(dto));
    }

    /* PATCH */

    @PatchMapping("/{id}/date")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updateDate(@PathVariable final Long id, @RequestBody @Valid final EventDateDto dto) {
        if (this.eventService.updateDate(id, this.eventDateConverter.toEntity().apply(dto))) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/number")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updateNumber(@PathVariable final Long id,
                                             @RequestBody @Valid final EventNumberDto dto) {
        if (this.eventService.updateNumber(id, this.eventNumberConverter.toEntity().apply(dto))) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/categories")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updateCategories(@PathVariable final Long id,
                                                 @RequestBody final EventCategoryDto dto) {
        if (this.eventService.updateCategories(id, this.eventCategoryConverter.toEntity().apply(dto)))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/join")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> join(@PathVariable final Long id) {
        if (this.eventService.joinUserToEvent(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/divorce")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> divorce(@PathVariable final Long id) {
        if (this.eventService.divorceUserFromEvent(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> removeUserFromEvent(@PathVariable final Long id, @PathVariable final Long userId) {
        if (this.eventService.removeUserFromEvent(id, userId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    /* DELETE */

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        if (this.eventService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
