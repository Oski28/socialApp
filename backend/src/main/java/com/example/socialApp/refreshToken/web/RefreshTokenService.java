package com.example.socialApp.refreshToken.web;

public interface RefreshTokenService {

    void removeExpiredTokens() throws InterruptedException;
}
