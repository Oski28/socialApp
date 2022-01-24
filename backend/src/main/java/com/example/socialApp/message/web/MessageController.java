package com.example.socialApp.message.web;

import com.example.socialApp.message.dto.MessageDto;
import com.example.socialApp.message.dto.MessageShowDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
public class MessageController {

    private final MessageServiceImplementation messageService;

    @Autowired
    public MessageController(MessageServiceImplementation messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("chat/{id}")
    @SendTo("/topic/chat/{id}/sent")
    public MessageShowDto sendMessage(@DestinationVariable("id") final Long id, @Payload @Valid MessageDto dto) {
        return this.messageService.saveAndReturnShowDto(dto, id);
    }

    @MessageExceptionHandler
    @SendToUser("/topic/error")
    public String handleException(Exception exception) {
        return exception.getMessage();
    }
}
