package com.example.socialApp.passwordToken.web;

import com.example.socialApp.passwordToken.model_repo.PasswordToken;
import com.example.socialApp.passwordToken.model_repo.PasswordTokenRepository;
import com.example.socialApp.payload.request.PasswordTokenRequest;
import com.example.socialApp.refreshToken.model_repo.RefreshToken;
import com.example.socialApp.user.web.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordTokenServiceImplementation implements PasswordTokenService {

    private final static int PASSWORD_TOKEN_EXPIRATION_TIME_IN_MINUTES = 15;
    private final PasswordTokenRepository passwordTokenRepository;
    private UserServiceImplementation userService;

    @Autowired
    public PasswordTokenServiceImplementation(PasswordTokenRepository passwordTokenRepository) {
        this.passwordTokenRepository = passwordTokenRepository;
    }

    @Autowired
    public void setUserService(UserServiceImplementation userService) {
        this.userService = userService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void removeExpiredTokens() {
        List<PasswordToken> passwordTokens = this.passwordTokenRepository.getAllByExpiryDateBefore(LocalDateTime.now());
        passwordTokens.forEach(
                token -> {
                    if (token.getExpiryDate().compareTo(LocalDateTime.now()) < 0) {
                        passwordTokenRepository.delete(token);
                    }
                });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public PasswordToken createToken(String username) {
        PasswordToken passwordToken = new PasswordToken();

        passwordToken.setUser(this.userService.findByUsername(username));
        passwordToken.setExpiryDate(LocalDateTime.now().plusMinutes(PASSWORD_TOKEN_EXPIRATION_TIME_IN_MINUTES));
        passwordToken.setToken(UUID.randomUUID().toString());
        passwordToken = passwordTokenRepository.save(passwordToken);
        return passwordToken;
    }

    @Override
    public boolean verifyRequest(PasswordTokenRequest passwordTokenRequest) {
        return this.userService.existByUsernameAndEmail(passwordTokenRequest.getUsername(), passwordTokenRequest.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public PasswordToken findByToken(String token) {
        Optional<PasswordToken> passwordToken = this.passwordTokenRepository.findByToken(token);
        return passwordToken.orElse(null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void removeToken(PasswordToken passwordToken) {
        this.passwordTokenRepository.delete(passwordToken);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean isExpired(PasswordToken passwordToken) {
        if (passwordToken.getExpiryDate().compareTo(LocalDateTime.now()) < 0) {
            passwordTokenRepository.delete(passwordToken);
            return true;
        } else return false;
    }
}
