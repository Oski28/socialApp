package com.example.socialApp.payload.request;

import com.example.socialApp.validator.ValidPassword;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
public class SignupRequest {

    private String avatar;

    @NotBlank(message = "Firstname cannot be blank.")
    @Size(min = 3, max = 50, message = "Firstname must contain beetwen 3 and 50 characters.")
    private String firstname;

    @NotBlank(message = "Lastname cannot be blank.")
    @Size(min = 3, max = 50, message = "Lastname must contain beetwen 3 and 50 characters.")
    private String lastname;

    @NotBlank(message = "Username cannot be blank.")
    @Size(min = 3, max = 50, message = "Username must contain beetwen 3 and 50 characters.")
    private String username;

    @Email(message = "Email must be correct.")
    private String email;

    @ValidPassword
    private String password;

    @NotNull(message = "Date of birth cannot be null.")
    private LocalDate dateOfBirth;

    private Set<String> roles;
}
