package com.example.socialApp.payload.request;

import javax.validation.constraints.NotBlank;

public class PasswordTokenRequest {

    @NotBlank
    String username;

    @NotBlank
    String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
