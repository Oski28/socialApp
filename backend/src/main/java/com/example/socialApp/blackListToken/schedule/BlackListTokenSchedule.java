package com.example.socialApp.blackListToken.schedule;

import com.example.socialApp.blackListToken.web.BlackListTokenService;
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
public class BlackListTokenSchedule {

    private final BlackListTokenService blackListTokenService;

    @Async
    @Scheduled(cron = "0 0/15 * * * *") // every 15 minute
    public void removeExpiredTokens() {
        this.blackListTokenService.removeExpiredTokens();
    }
}
