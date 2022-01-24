package com.example.socialApp.registerToken.schedule;

import com.example.socialApp.registerToken.web.RegisterTokenService;
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
public class RegisterTokenSchedule {

    private final RegisterTokenService registerTokenService;

    @Async
    @Scheduled(cron = "0 0/10 * * * *") // every 10 minute
    public void removeExpiredTokens() {
        this.registerTokenService.removeExpiredTokens();
    }
}
