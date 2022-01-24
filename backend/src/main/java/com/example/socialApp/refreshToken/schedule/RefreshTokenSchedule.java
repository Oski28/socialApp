package com.example.socialApp.refreshToken.schedule;

import com.example.socialApp.refreshToken.web.RefreshTokenService;
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
public class RefreshTokenSchedule {

    private final RefreshTokenService refreshTokenService;

    @Async
    @Scheduled(cron = "0 * * * * *") // every minute
    public void removeExpiredTokens() throws InterruptedException {
        this.refreshTokenService.removeExpiredTokens();
    }
}
