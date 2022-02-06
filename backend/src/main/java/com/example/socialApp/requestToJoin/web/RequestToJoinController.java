package com.example.socialApp.requestToJoin.web;

import com.example.socialApp.exceptions.DuplicateObjectException;
import com.example.socialApp.requestToJoin.converter.RequestToJoinShowForEventConverter;
import com.example.socialApp.requestToJoin.converter.RequestToJoinShowForUserConverter;
import com.example.socialApp.requestToJoin.converter.RequestToJoinUpdateConverter;
import com.example.socialApp.requestToJoin.dto.RequestToJoinDto;
import com.example.socialApp.requestToJoin.dto.RequestToJoinShowForEventDto;
import com.example.socialApp.requestToJoin.dto.RequestToJoinShowForUserDto;
import com.example.socialApp.requestToJoin.dto.RequestToJoinUpdateDto;
import com.example.socialApp.requestToJoin.model_repo.RequestToJoin;
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

@RestController
@RequestMapping(path = "/api/requestToJoin")
public class RequestToJoinController extends BaseController<RequestToJoin> {

    private final RequestToJoinServiceImplementation requestToJoinService;

    private final RequestToJoinShowForEventConverter requestToJoinShowForEventConverter;

    private final RequestToJoinShowForUserConverter requestToJoinShowForUserConverter;

    private final UserServiceImplementation userService;

    private final RequestToJoinUpdateConverter requestToJoinUpdateConverter;

    @Autowired
    public RequestToJoinController(BaseService<RequestToJoin> service, RequestToJoinServiceImplementation requestToJoinService,
                                   RequestToJoinShowForEventConverter requestToJoinShowForEventConverter,
                                   RequestToJoinShowForUserConverter requestToJoinShowForUserConverter, UserServiceImplementation userService,
                                   RequestToJoinUpdateConverter requestToJoinUpdateConverter) {
        super(service);
        this.requestToJoinService = requestToJoinService;
        this.requestToJoinShowForEventConverter = requestToJoinShowForEventConverter;
        this.requestToJoinShowForUserConverter = requestToJoinShowForUserConverter;
        this.userService = userService;
        this.requestToJoinUpdateConverter = requestToJoinUpdateConverter;
    }

    /* GET */

    @GetMapping("{eventId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<RequestToJoinShowForEventDto>> getAllForEvent(@PathVariable final Long eventId,
                                                                             @RequestParam(defaultValue = "0") final int page,
                                                                             @RequestParam(defaultValue = "20") final int size,
                                                                             @RequestParam(defaultValue = "id") final String column,
                                                                             @RequestParam(defaultValue = "ASC") final String direction) {
        Sort.Direction sortDir = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Page<RequestToJoin> requestToJoinPage = this.requestToJoinService.getAllWaitingForEvent(page, size, column, sortDir, eventId);
        return ResponseEntity.ok(requestToJoinPage
                .map(this.requestToJoinShowForEventConverter.toDto()));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<RequestToJoinShowForUserDto>> getAllForUser(@RequestParam(defaultValue = "0") final int page,
                                                                            @RequestParam(defaultValue = "20") final int size,
                                                                            @RequestParam(defaultValue = "id") final String column,
                                                                            @RequestParam(defaultValue = "ASC") final String direction) {
        Sort.Direction sortDir = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Page<RequestToJoin> requestToJoinPage = this.requestToJoinService.getAllWaitingForUser(page, size, column, sortDir);
        return ResponseEntity.ok(requestToJoinPage
                .map(this.requestToJoinShowForUserConverter.toDto()));
    }

    /* POST */

    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@RequestBody final RequestToJoinDto dto) {
        User user = this.userService.getAuthUser();
        if (this.requestToJoinService.existByUserEvent(user, dto.getEventId())) {
            throw new DuplicateObjectException("Prośba o dołączenie do tego wydarzenia "
                    + "przez użytkownika została wysłana wcześniej.");
        }
        RequestToJoin requestToJoin = this.requestToJoinService.create(user, dto.getEventId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(requestToJoin.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    /* PATCH */
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateStatus(@PathVariable final Long id,
                                          @RequestBody @Valid RequestToJoinUpdateDto dto) {
        if (this.requestToJoinService.update(id, this.requestToJoinUpdateConverter.toEntity().apply(dto))) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/resend")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> resend(@PathVariable final Long id) {
        if (this.requestToJoinService.resend(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
