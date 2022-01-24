package com.example.socialApp.user.converter;

import com.example.socialApp.shared.BaseConverter;
import com.example.socialApp.user.dto.UserAvatarDto;
import com.example.socialApp.user.model_repo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserAvatarConverter extends BaseConverter<User, UserAvatarDto> {

    @Override
    public Function<UserAvatarDto, User> toEntity() {
        return dto -> {
            User user = new User();
            convertIfNotNull(user::setAvatar, dto::getAvatar);
            return user;
        };
    }

    @Override
    public Function<User, UserAvatarDto> toDto() {
        return user -> {
            UserAvatarDto dto = new UserAvatarDto();
            convertIfNotNull(dto::setAvatar, user::getAvatar);
            return dto;
        };
    }
}
