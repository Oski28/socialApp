package com.example.socialApp.message.converter;

import com.example.socialApp.message.dto.MessageShowDto;
import com.example.socialApp.message.model_repo.Message;
import com.example.socialApp.shared.BaseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class MessageShowConverter extends BaseConverter<Message, MessageShowDto> {


    @Override
    public Function<MessageShowDto, Message> toEntity() {
        return null;
    }

    @Override
    public Function<Message, MessageShowDto> toDto() {
        return message -> {
            if (message == null)
                return null;

            MessageShowDto dto = new MessageShowDto();
            if (message.getFile() != null) {
                String fileType = message.getFile().substring(0, message.getFile().indexOf(";") + 1).toLowerCase();
                if (fileType.contains("png") || fileType.contains("jpeg") || fileType.contains("jpg")) {
                    dto.setFile(message.getFile());
                    dto.setFileToDownload(false);
                } else {
                    dto.setFile("http://localhost:9090/api/messages/" + message.getId() + "/file");
                    dto.setFileToDownload(true);
                }
            } else dto.setFileToDownload(false);
            dto.setId(message.getId());
            dto.setContent(message.getContent());
            dto.setWriteDate(message.getWriteDate());
            dto.setDeleteDate(message.getDeleteDate());
            dto.setSenderUsername(message.getUser().getUsername());
            dto.setAvatar(message.getUser().getAvatar());
            dto.setSenderId(message.getUser().getId());

            return dto;
        };
    }
}
