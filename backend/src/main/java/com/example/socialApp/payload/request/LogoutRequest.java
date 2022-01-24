package com.example.socialApp.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LogoutRequest {

    private Long userId;
    @NotBlank(message = "Token cannot be blank.")
    @Size(min = 1, max = 200, message = "Token must contain between 1 and 200 characters.")
    private String token;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
