package com.example.socialApp.user.converter;

import com.example.socialApp.shared.BaseConverter;
import com.example.socialApp.user.dto.UserEventDto;
import com.example.socialApp.user.model_repo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserEventConverter extends BaseConverter<User, UserEventDto> {

    @Override
    public Function<UserEventDto, User> toEntity() {
        return null;
    }

    @Override
    public Function<User, UserEventDto> toDto() {
        return user -> {
            if (user == null)
                return null;

            UserEventDto dto = new UserEventDto();

            dto.setAvatar(user.getAvatar());
            dto.setDateOfBirth(user.getDateOfBirth());
            dto.setEmail(user.getEmail());
            dto.setFirstname(user.getFirstname());
            dto.setId(user.getId());
            dto.setLastname(user.getLastname());
            dto.setBlocked(user.getBlocked());
            dto.setAddDate(user.getAddDate());
            dto.setUsername(user.getUsername());

            return dto;
        };
    }
}
