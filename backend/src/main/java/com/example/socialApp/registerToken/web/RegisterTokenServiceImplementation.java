package com.example.socialApp.registerToken.web;

import com.example.socialApp.registerToken.model_repo.RegisterToken;
import com.example.socialApp.registerToken.model_repo.RegisterTokenRepository;
import com.example.socialApp.user.model_repo.User;
import com.example.socialApp.user.web.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterTokenServiceImplementation implements RegisterTokenService {

    private final RegisterTokenRepository registerTokenRepository;
    private final UserServiceImplementation userService;

    @Override
    public RegisterToken create(User user) {
        RegisterToken token = new RegisterToken();
        token.setToken(UUID.randomUUID().toString());
        token.setCreatedDate(LocalDateTime.now());
        token.setUser(user);
        return this.registerTokenRepository.save(token);
    }

    @Override
    public RegisterToken findByToken(String token) {
        return this.registerTokenRepository.findByToken(token);
    }

    @Override
    public RegisterToken findByUser(User user) {
        return this.registerTokenRepository.findByUser(user);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void removeExpiredTokens() {
        List<RegisterToken> registerTokens = this.registerTokenRepository.findAll();
        registerTokens.forEach(
                registerToken -> {
                    User tokenUser = registerToken.getUser();
                    if ((registerToken.getCreatedDate().compareTo(LocalDateTime.now().minusDays(1)) < 0)
                            && !tokenUser.getEnabled() && tokenUser.getDeleteDate() == null) {
                        this.registerTokenRepository.delete(registerToken);
                        this.userService.delete(tokenUser.getId());
                    }
                }
        );
    }
}
