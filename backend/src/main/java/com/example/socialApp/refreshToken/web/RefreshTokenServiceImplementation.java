package com.example.socialApp.refreshToken.web;

import com.example.socialApp.exceptions.TokenException;
import com.example.socialApp.refreshToken.model_repo.RefreshToken;
import com.example.socialApp.refreshToken.model_repo.RefreshTokenRepository;
import com.example.socialApp.user.web.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImplementation implements RefreshTokenService {

    @Value("${pl.oski.jwtRefreshExpiration}")
    private Long refreshTokenDurationSeconds;

    private final RefreshTokenRepository refreshTokenRepository;
    private UserServiceImplementation userService;

    @Autowired
    public RefreshTokenServiceImplementation(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Autowired
    public void setUserService(UserServiceImplementation userService) {
        this.userService = userService;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userService.getById(userId));
        refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTokenDurationSeconds));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(LocalDateTime.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenException(token.getToken(), "Refresh token wygasł. Wymagane ponowne logowanie.");
        }

        return token;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void deleteByUserId(Long id) {
        refreshTokenRepository.deleteByUser(userService.getById(id));
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void removeExpiredTokens() {
        List<RefreshToken> refreshTokens = this.refreshTokenRepository.getAllByExpiryDateBefore(LocalDateTime.now());
        refreshTokens.forEach(
                token -> {
                    if (token.getExpiryDate().compareTo(LocalDateTime.now()) < 0) {
                        refreshTokenRepository.delete(token);
                    }
                });
    }

}
