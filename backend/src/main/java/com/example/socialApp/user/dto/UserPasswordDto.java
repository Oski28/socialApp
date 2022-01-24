package com.example.socialApp.user.dto;

import com.example.socialApp.validator.ValidPassword;
import lombok.Data;

@Data
public class UserPasswordDto {

    @ValidPassword
    private String oldPassword;

    @ValidPassword
    private String newPassword;
}
