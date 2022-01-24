package com.example.socialApp.passwordToken.dto;

import com.example.socialApp.validator.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PasswordDto {

    @ValidPassword
    private String password;

    @NotBlank
    private String token;
}
