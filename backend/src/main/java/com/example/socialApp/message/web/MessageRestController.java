package com.example.socialApp.message.web;


import com.example.socialApp.message.converter.MessageShowConverter;

import com.example.socialApp.message.dto.MessageFileDto;
import com.example.socialApp.message.dto.MessageShowDto;
import com.example.socialApp.message.model_repo.Message;
import com.example.socialApp.shared.BaseController;
import com.example.socialApp.shared.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/api/messages")
public class MessageRestController extends BaseController<Message> {

    private final MessageServiceImplementation messageService;

    private final MessageShowConverter messageShowConverter;

    @Autowired
    public MessageRestController(BaseService<Message> service, MessageServiceImplementation messageService,
                                 MessageShowConverter messageShowConverter) {
        super(service);
        this.messageService = messageService;
        this.messageShowConverter = messageShowConverter;
    }

    /* GET */
    @GetMapping("/{id}/file")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Resource> downloadFile(@PathVariable final Long id) {
        MessageFileDto dto = this.messageService.getFile(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dto.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dto.getFileName() + "\"")
                .body(new ByteArrayResource(dto.getFileData()));
    }

    @GetMapping("/{id}/file/name")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> getName(@PathVariable final Long id) {
        MessageFileDto dto = this.messageService.getFile(id);
        return ResponseEntity.ok(dto.getFileName());
    }

    @GetMapping("/{chatId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<MessageShowDto>> getAll(@RequestParam(defaultValue = "0") final int page,
                                                       @RequestParam(defaultValue = "20") final int size,
                                                       @RequestParam(defaultValue = "writeDate") final String column,
                                                       @RequestParam(defaultValue = "DESC") final String direction,
                                                       @PathVariable final Long chatId) {
        Sort.Direction sortDir = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(new PageImpl<>(this.messageService.getAllForChat(page, size, column, sortDir, chatId)
                .map(this.messageShowConverter.toDto()).stream()
                .sorted(Comparator.comparingLong(MessageShowDto::getId)).collect(Collectors.toList())));
    }

    /* DELETE */

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        if (this.messageService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
