package com.example.socialApp.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class BanRequest {

    @NotNull(message = "Ban expiration date cannot be null.")
    LocalDate banExpirationDate;
}
