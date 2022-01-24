package com.example.socialApp.passwordToken.web;

import com.example.socialApp.passwordToken.model_repo.PasswordToken;
import com.example.socialApp.payload.request.PasswordTokenRequest;

public interface PasswordTokenService {

    void removeExpiredTokens();

    PasswordToken createToken(String username);

    boolean verifyRequest(PasswordTokenRequest passwordTokenRequest);

    PasswordToken findByToken(String token);

    void removeToken(PasswordToken passwordToken);

    boolean isExpired(PasswordToken passwordToken);
}
