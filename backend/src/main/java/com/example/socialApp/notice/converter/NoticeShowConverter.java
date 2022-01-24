package com.example.socialApp.notice.converter;

import com.example.socialApp.notice.dto.NoticeShowDto;
import com.example.socialApp.notice.model_repo.Notice;
import com.example.socialApp.shared.BaseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class NoticeShowConverter extends BaseConverter<Notice, NoticeShowDto> {


    @Override
    public Function<NoticeShowDto, Notice> toEntity() {
        return null;
    }

    @Override
    public Function<Notice, NoticeShowDto> toDto() {
        return notice -> {
            if (notice == null)
                return null;

            NoticeShowDto dto = new NoticeShowDto();
            dto.setId(notice.getId());
            dto.setContent(notice.getContent());
            dto.setReceived(notice.getReceived());

            return dto;
        };
    }
}
