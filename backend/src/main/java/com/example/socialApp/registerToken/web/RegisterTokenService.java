package com.example.socialApp.registerToken.web;

import com.example.socialApp.registerToken.model_repo.RegisterToken;
import com.example.socialApp.user.model_repo.User;

public interface RegisterTokenService {

    RegisterToken create(User user);

    RegisterToken findByToken(String token);

    RegisterToken findByUser(User user);

    void removeExpiredTokens();
}
