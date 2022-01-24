package com.example.socialApp.user.schedule;

import com.example.socialApp.user.web.UserService;
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
public class UserBanSchedule {

    private final UserService userService;

    @Async
    @Scheduled(cron = "* 1 0 * * *") // every day at 00:01
    public void refreshBan() {
        this.userService.unbanIfBanDateExpired();
    }
}
