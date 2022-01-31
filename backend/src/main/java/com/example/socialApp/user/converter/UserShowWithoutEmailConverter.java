package com.example.socialApp.user.converter;

import com.example.socialApp.chat.converter.ChatNameConverter;
import com.example.socialApp.event.converter.EventShowConverter;
import com.example.socialApp.role.converter.RoleShowConverter;
import com.example.socialApp.shared.BaseConverter;
import com.example.socialApp.user.dto.UserShowDto;
import com.example.socialApp.user.model_repo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserShowWithoutEmailConverter extends BaseConverter<User, UserShowDto> {

    private final RoleShowConverter roleShowConverter;
    private final ChatNameConverter chatNameConverter;
    private final EventShowConverter eventShowConverter;

    @Override
    public Function<UserShowDto, User> toEntity() {
        return null;
    }

    @Override
    public Function<User, UserShowDto> toDto() {
        return user -> {
            if (user == null)
                return null;

            UserShowDto dto = new UserShowDto();
            dto.setAvatar(user.getAvatar());
            dto.setChats(
                    user.getChats()
                            .stream()
                            .map(this.chatNameConverter.toDto())
                            .collect(Collectors.toSet()));
            dto.setDateOfBirth(user.getDateOfBirth());
            dto.setEmail(null);
            dto.setFirstname(user.getFirstname());
            dto.setId(user.getId());
            dto.setLastname(user.getLastname());
            dto.setRoles(
                    user.getRoles()
                            .stream()
                            .map(this.roleShowConverter.toDto())
                            .collect(Collectors.toSet()));
            dto.setUserCreatedEvents(
                    user.getUserCreatedEvents()
                            .stream()
                            .map(this.eventShowConverter.toDto())
                            .collect(Collectors.toList()));
            dto.setUsername(user.getUsername());
            dto.setUserParticipatedEvents(
                    user.getUserParticipatedEvents()
                            .stream()
                            .map(this.eventShowConverter.toDto())
                            .collect(Collectors.toList()));
            dto.setBlocked(user.getBlocked());
            dto.setAddDate(user.getAddDate());
            dto.setBanExpirationDate(user.getBanExpirationDate());
            dto.setEnabled(user.getEnabled());

            return dto;
        };
    }
}
