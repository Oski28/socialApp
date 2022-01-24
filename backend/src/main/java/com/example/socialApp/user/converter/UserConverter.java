package com.example.socialApp.user.converter;

import com.example.socialApp.payload.request.SignupRequest;
import com.example.socialApp.role.web.RoleServiceImplementation;
import com.example.socialApp.user.model_repo.User;
import com.example.socialApp.shared.BaseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Function;

@Service
public class UserConverter extends BaseConverter<User, SignupRequest> {


    private final PasswordEncoder encoder;

    private final RoleServiceImplementation roleService;

    @Autowired
    public UserConverter(PasswordEncoder encoder, RoleServiceImplementation roleService) {
        this.encoder = encoder;
        this.roleService = roleService;
    }


    @Override
    public Function<SignupRequest, User> toEntity() {
        return signupRequest -> {
            if (signupRequest == null)
                return null;

            User user = new User();
            user.setUsername(signupRequest.getUsername());
            user.setAvatar(signupRequest.getAvatar());
            user.setDateOfBirth(signupRequest.getDateOfBirth());
            user.setEmail(signupRequest.getEmail());
            user.setFirstname(signupRequest.getFirstname());
            user.setLastname(signupRequest.getLastname());
            user.setPassword(encoder.encode(signupRequest.getPassword()));
            user.setBanExpirationDate(null);
            user.setBlocked(false);
            user.setDeleteDate(null);
            user.setEnabled(false);
            user.setRoles(roleService.getRolesFromStringsSet(signupRequest.getRoles()));
            user.setAddDate(LocalDateTime.now());
            return user;
        };
    }

    @Override
    public Function<User, SignupRequest> toDto() {
        return null;
    }
}
