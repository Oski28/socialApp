package com.example.socialApp.event.model_repo;

import com.example.socialApp.shared.BaseRepository;
import com.example.socialApp.user.model_repo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EventRepository extends BaseRepository<Event> {

    Page<Event> findAllByDateAfterAndAgeLimitLessThanOrDateAfterAndAgeLimitIsNull(
            LocalDateTime dateTime1, Integer limit, LocalDateTime dateTime2, Pageable pageable);

    Page<Event> getAllByNameContainsAndDateAfterAndAgeLimitLessThanOrNameContainsAndDateAfterAndAgeLimitIsNull(
            String filter1, LocalDateTime dateTime1, Integer limit, String filter2, LocalDateTime dateTime2,
            Pageable pageable);

    Page<Event> getAllByUsersContainingAndDateAfterAndAgeLimitLessThanOrUsersContainingAndDateAfterAndAgeLimitIsNull(
            User user1, LocalDateTime dateTime1, Integer limit, User user2, LocalDateTime dateTime2, Pageable pageable);

    Page<Event> getAllByUsersContainingAndNameContainsAndDateAfterAndAgeLimitLessThanOrUsersContainingAndNameContainsAndDateAfterAndAgeLimitIsNull(
            User user1, String filter1, LocalDateTime dateTime1, Integer limit, User user2, String filter2,
            LocalDateTime dateTime2, Pageable pageable);

    Page<Event> findAllByDateBeforeAndAgeLimitLessThanOrDateBeforeAndAgeLimitIsNull(
            LocalDateTime dateTime1, Integer limit, LocalDateTime dateTime2, Pageable pageable);

    Page<Event> getAllByNameContainsAndDateBeforeAndAgeLimitLessThanOrNameContainsAndDateBeforeAndAgeLimitIsNull(
            String filter1, LocalDateTime dateTime1, Integer limit, String filter2, LocalDateTime dateTime2,
            Pageable pageable);

    Page<Event> getAllByUsersContainingAndDateBeforeAndAgeLimitLessThanOrUsersContainingAndDateBeforeAndAgeLimitIsNull(
            User user1, LocalDateTime dateTime1, Integer limit, User user2, LocalDateTime dateTime2, Pageable pageable);

    Page<Event> getAllByUsersContainingAndNameContainsAndDateBeforeAndAgeLimitLessThanOrUsersContainingAndNameContainsAndDateBeforeAndAgeLimitIsNull(
            User user1, String filter1, LocalDateTime dateTime1, Integer limit, User user2, String filter2,
            LocalDateTime dateTime2, Pageable pageable);

    Boolean existsByName(String name);

    Page<Event> getAllByUserAndDateAfter(User user, LocalDateTime dateTime, Pageable pageable);

    Page<Event> getAllByUserAndDateBefore(User user, LocalDateTime dateTime, Pageable pageable);

    Page<Event> getAllByUserAndNameContainsAndDateAfter(User user, String filter, LocalDateTime dateTime, Pageable pageable);

    Page<Event> getAllByUserAndNameContainsAndDateBefore(User user, String filter, LocalDateTime dateTime, Pageable pageable);

    Page<Event> getAllByUsersNotContainingAndNameContainsAndDateAfterAndAgeLimitLessThanOrUsersNotContainingAndNameContainsAndDateAfterAndAgeLimitIsNull(
            User user1, String filter1, LocalDateTime localDateTime1,
            Integer ageLimit1, User user2, String filter2, LocalDateTime localDateTime2, Pageable pageable
    );

    Page<Event> getAllByUsersNotContainingAndNameContainsAndDateBeforeAndAgeLimitLessThanOrUsersNotContainingAndNameContainsAndDateBeforeAndAgeLimitIsNull(
            User user1, String filter1, LocalDateTime localDateTime1,
            Integer ageLimit1, User user2, String filter2, LocalDateTime localDateTime2, Pageable pageable
    );

    Page<Event> getAllByUsersNotContainingAndDateAfterAndAgeLimitLessThanOrUsersNotContainingAndDateAfterAndAgeLimitIsNull(
            User user1, LocalDateTime localDateTime1,
            Integer ageLimit1, User user2, LocalDateTime localDateTime2, Pageable pageable
    );

    Page<Event> getAllByUsersNotContainingAndDateBeforeAndAgeLimitLessThanOrUsersNotContainingAndDateBeforeAndAgeLimitIsNull(
            User user1, LocalDateTime localDateTime1,
            Integer ageLimit1, User user2, LocalDateTime localDateTime2, Pageable pageable
    );
}
