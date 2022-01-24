package com.example.socialApp.user.model_repo;

import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.shared.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User> {

    User findByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByUsernameAndEmail(String username, String email);

    Page<User> findAllByFirstnameContainsAndEnabledTrueAndUsernameNotOrLastnameContainsAndEnabledTrueAndUsernameNot(String firstname, String username1, String lastname, String username2, Pageable pageable);

    Page<User> findAllByEnabledTrueAndUsernameNot(String username, Pageable pageable);

    Page<User> findAllByUserParticipatedEventsContains(Event event, Pageable pageable);
}
