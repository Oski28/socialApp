package com.example.socialApp.passwordToken.schedule;

import com.example.socialApp.passwordToken.web.PasswordTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
@EnableScheduling
@EnableAsync
public class PasswordTokenSchedule {

    private final PasswordTokenService passwordTokenService;

    @Async
    @Scheduled(cron = "0 * * * * *") // every minute
    public void removeExpiredTokens() {
        this.passwordTokenService.removeExpiredTokens();
    }
}
