package com.example.socialApp.requestToJoin.model_repo;

import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.shared.BaseRepository;
import com.example.socialApp.user.model_repo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestToJoinRepository extends BaseRepository<RequestToJoin> {

    List<RequestToJoin> getAllByUser(User user);

    List<RequestToJoin> getAllByEvent(Event event);

    Page<RequestToJoin> getAllByEventAndStatusIs(Event event, Status status, Pageable pageable);

    Boolean existsByEventAndUser(Event event, User user);

    Page<RequestToJoin> getAllByUser(User user, Pageable pageable);
}
