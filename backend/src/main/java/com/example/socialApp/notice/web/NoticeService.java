package com.example.socialApp.notice.web;

import com.example.socialApp.notice.model_repo.Notice;
import com.example.socialApp.user.model_repo.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;

import java.util.Set;


public interface NoticeService {

    void removeNoticeForDeletedUser(User user);

    Page<Notice> getAllNotReceivedForUser(int page, int size, String column, Sort.Direction sortDir);

    Page<Notice> getAllForUser(int page, int size, String column, Sort.Direction sortDir);

    void received(Long id);

    void addNoticeToUsers(Notice notice, Set<User> users);

    Notice create(String content);
}
