package com.example.socialApp.user.converter;

import com.example.socialApp.role.model_repo.Role;
import com.example.socialApp.role.web.RoleServiceImplementation;
import com.example.socialApp.shared.BaseConverter;
import com.example.socialApp.user.dto.UserRoleDto;
import com.example.socialApp.user.model_repo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoleConverter extends BaseConverter<User, UserRoleDto> {

    private final RoleServiceImplementation roleService;

    @Override
    public Function<UserRoleDto, User> toEntity() {
        return dto -> {
            if (dto == null)
                return null;

            User user = new User();
            user.setRoles(roleService.getRolesFromStringsSet(dto.getRoles()));

            return user;
        };
    }

    @Override
    public Function<User, UserRoleDto> toDto() {
        return user -> {
            if (user == null)
                return null;

            UserRoleDto dto = new UserRoleDto();

            dto.setRoles(user.getRoles().stream().map(Role::getRole).map(Enum::toString).collect(Collectors.toSet()));

            return dto;
        };
    }
}
