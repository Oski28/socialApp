package com.example.socialApp.notice.web;

import com.example.socialApp.exceptions.OperationAccessDeniedException;
import com.example.socialApp.notice.converter.NoticeShowConverter;
import com.example.socialApp.notice.dto.NoticeShowDto;
import com.example.socialApp.notice.model_repo.Notice;
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

@RestController
@RequestMapping(path = "/api/notices")
@CrossOrigin
public class NoticeController extends BaseController<Notice> {

    private final NoticeServiceImplementation noticeService;

    private final NoticeShowConverter noticeShowConverter;

    private final UserServiceImplementation userService;

    @Autowired
    public NoticeController(BaseService<Notice> service, NoticeServiceImplementation noticeService,
                            NoticeShowConverter noticeShowConverter, UserServiceImplementation userService) {
        super(service);
        this.noticeService = noticeService;
        this.noticeShowConverter = noticeShowConverter;
        this.userService = userService;
    }

    /* GET */

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<NoticeShowDto> getOne(@PathVariable final Long id) {
        Notice notice = this.noticeService.getById(id);
        User user = this.userService.getAuthUser();
        if (notice.getUser().equals(user)) {
            this.noticeService.received(id);
            return super.getOne(id, this.noticeShowConverter.toDto());
        } else throw new OperationAccessDeniedException("Brak uprawnień do pobrania danych powiadomienia o id " + id);
    }

    @GetMapping("")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Page<NoticeShowDto>> getAll(@RequestParam(defaultValue = "0") final int page,
                                                      @RequestParam(defaultValue = "20") final int size,
                                                      @RequestParam(defaultValue = "id") final String column,
                                                      @RequestParam(defaultValue = "ASC") final String direction) {
        Sort.Direction sortDir = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(this.noticeService.getAll(page, size, column, sortDir)
                .map(this.noticeShowConverter.toDto()));
    }

    @GetMapping("/user/notReceived")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<NoticeShowDto>> getAllNotReceivedForAuthUser(@RequestParam(defaultValue = "0") final int page,
                                                                            @RequestParam(defaultValue = "20") final int size,
                                                                            @RequestParam(defaultValue = "id") final String column,
                                                                            @RequestParam(defaultValue = "ASC") final String direction) {
        Sort.Direction sortDir = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(this.noticeService.getAllNotReceivedForUser(page, size, column, sortDir)
                .map(this.noticeShowConverter.toDto()));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<NoticeShowDto>> getAllForAuthUser(@RequestParam(defaultValue = "0") final int page,
                                                                 @RequestParam(defaultValue = "20") final int size,
                                                                 @RequestParam(defaultValue = "id") final String column,
                                                                 @RequestParam(defaultValue = "ASC") final String direction) {
        Sort.Direction sortDir = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(this.noticeService.getAllForUser(page, size, column, sortDir)
                .map(this.noticeShowConverter.toDto()));
    }

    /* DELETE */

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        if (this.noticeService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
