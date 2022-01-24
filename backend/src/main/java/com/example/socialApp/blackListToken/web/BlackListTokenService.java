package com.example.socialApp.blackListToken.web;

import com.example.socialApp.blackListToken.model_repo.BlackListToken;
import com.example.socialApp.user.model_repo.User;

public interface BlackListTokenService {

    BlackListToken create(String token, User user);

    void removeExpiredTokens();

    Boolean isExistByToken(String token);
}
