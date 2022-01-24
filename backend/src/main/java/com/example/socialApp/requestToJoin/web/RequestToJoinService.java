package com.example.socialApp.requestToJoin.web;

import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.requestToJoin.model_repo.RequestToJoin;
import com.example.socialApp.user.model_repo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface RequestToJoinService {

    void removeRequestToJoinForDeletedUser(User user);

    void removeRequestToJoinForDeletedEvent(Event event);

    Page<RequestToJoin> getAllWaitingForEvent(int page, int size, String column, Sort.Direction direction, Long eventId);

    boolean existByUserEvent(User user, Long eventId);

    RequestToJoin create(User user, Long eventId);

    void updateAllForEvent(Event event);

    Page<RequestToJoin> getAllWaitingForUser(int page, int size, String column, Sort.Direction sortDir);

    boolean resend(Long id);
}
