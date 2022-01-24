package com.example.socialApp.event.web;

import com.example.socialApp.category.model_repo.Category;
import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.user.model_repo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.nio.file.AccessDeniedException;

public interface EventService {

    void removeEventsForDeletedUser(User user);

    void removeUserParticipationForAllEvents(User user);

    void removeCategoryForAllEvents(Category category);

    Page<Event> getAll(int page, int size, String column, Sort.Direction direction, String filter, Boolean activeDate);

    Page<Event> getAllForUserParticipate(int page, int size, String column, Sort.Direction direction, String filter,
                                         Boolean activeDate);

    Page<Event> getAllForUserCreate(int page, int size, String column, Sort.Direction direction, String filter,
                                    Boolean activeDate);

    Boolean existByName(String name);

    void addUserToEventParticipation(Long id, User user);

    boolean updateDate(Long id, Event event) throws AccessDeniedException;

    boolean updateNumber(Long id, Event apply);

    boolean updateCategories(Long id, Event apply);

    boolean joinUserToEvent(Long id);

    boolean divorceUserFromEvent(Long id);

    Page<Event> getAllForAuthUserNonParticipate(int page, int size, String column, Sort.Direction sortDir, String filter,
                                                boolean activeDate);

    boolean removeUserFromEvent(Long id, Long userId);
}
