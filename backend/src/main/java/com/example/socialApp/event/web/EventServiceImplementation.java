package com.example.socialApp.event.web;

import com.example.socialApp.category.model_repo.Category;
import com.example.socialApp.chat.web.ChatServiceImplementation;
import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.event.model_repo.EventRepository;
import com.example.socialApp.exceptions.AgeLimitException;
import com.example.socialApp.exceptions.EventParticipantLimitException;
import com.example.socialApp.exceptions.OperationAccessDeniedException;
import com.example.socialApp.notice.web.NoticeServiceImplementation;
import com.example.socialApp.report.web.ReportServiceImplementation;
import com.example.socialApp.requestToJoin.web.RequestToJoinServiceImplementation;
import com.example.socialApp.role.model_repo.ERole;
import com.example.socialApp.role.web.RoleServiceImplementation;
import com.example.socialApp.shared.BaseService;
import com.example.socialApp.user.model_repo.User;
import com.example.socialApp.user.web.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventServiceImplementation implements EventService, BaseService<Event> {

    private final EventRepository eventRepository;

    private ReportServiceImplementation reportService;

    private RequestToJoinServiceImplementation requestToJoinService;

    private ChatServiceImplementation chatService;

    private UserServiceImplementation userService;

    private RoleServiceImplementation roleService;

    private NoticeServiceImplementation noticeService;

    @Autowired
    public EventServiceImplementation(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Autowired
    public void setRequestToJoinService(RequestToJoinServiceImplementation requestToJoinService) {
        this.requestToJoinService = requestToJoinService;
    }

    @Autowired
    public void setNoticeService(NoticeServiceImplementation noticeService) {
        this.noticeService = noticeService;
    }

    @Autowired
    public void setReportService(ReportServiceImplementation reportService) {
        this.reportService = reportService;
    }

    @Autowired
    public void setChatService(ChatServiceImplementation chatService) {
        this.chatService = chatService;
    }

    @Autowired
    public void setUserService(UserServiceImplementation userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleServiceImplementation roleService) {
        this.roleService = roleService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Event> getAll(int page, int size, String column, Sort.Direction direction) {
        Sort sort = Sort.by(new Sort.Order(direction, column));
        return this.eventRepository.findAll(PageRequest.of(page, size, sort));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean update(Long id, Event entity) {
        if (isExists(id)) {
            User user = this.userService.getAuthUser();
            Event event = getById(id);

            if (!(event.getUser().equals(user) ||
                    user.getRoles().contains(this.roleService.findByRole(ERole.ROLE_MODERATOR)))) {
                throw new OperationAccessDeniedException("Brak dostępu do edycji eventu o id " + id);
            }
            if (entity.getFreeJoin().equals(true) && event.getFreeJoin().equals(false)) {
                this.requestToJoinService.updateAllForEvent(event);
            }

            Set<User> toRemove = new HashSet<>();
            for (User eventUser : event.getUsers()) {
                if (!checkAge(eventUser, entity)) {
                    toRemove.add(eventUser);
                }
            }
            event.getUsers().removeAll(toRemove);

            this.noticeService.addNoticeToUsers(
                    this.noticeService.create("Zaktualizowano dane wydarzenia " + event.getName() + " w którym bierzesz udział."),
                    event.getUsers());

            event.setName(entity.getName());
            event.setAgeLimit(entity.getAgeLimit());
            event.setFreeJoin(entity.getFreeJoin());
            event.setLocation(entity.getLocation());
            event.setDescription(entity.getDescription());

            return true;
        } else return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean delete(Long id) {

        if (isExists(id)) {
            User user = this.userService.getAuthUser();
            Event event = getById(id);

            if (!(event.getUser().equals(user) ||
                    user.getRoles().contains(this.roleService.findByRole(ERole.ROLE_MODERATOR)))) {
                throw new OperationAccessDeniedException("Brak dostępu do usuwania eventu o id " + id);
            }

            this.noticeService.addNoticeToUsers(
                    this.noticeService.create("Usunięto wydarzenie " + event.getName() + " w którym brałeś udział."),
                    event.getUsers());

            this.reportService.deleteAllForDeletedEvent(event);
            removeByIterator(event.getUsers());
            removeByIterator(event.getCategories());
            this.requestToJoinService.removeRequestToJoinForDeletedEvent(event);
            this.chatService.prepareChatToRemoveForDeletedEvent(event);

            this.eventRepository.delete(event);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Event save(Event entity) {
        return this.eventRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Event getById(Long id) {
        if (isExists(id)) {
            return this.eventRepository.getById(id);
        } else {
            return null;
        }
    }

    @Override
    public boolean isExists(Long id) {
        return this.eventRepository.existsById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void removeEventsForDeletedUser(User user) {
        List<Event> events = this.eventRepository.findAll().stream()
                .filter(event -> event.getUser().getId().equals(user.getId())).collect(Collectors.toList());
        for (Event event : events) {
            this.reportService.deleteAllForDeletedEvent(event);
            Set<User> eventUsers = event.getUsers();
            this.noticeService.addNoticeToUsers(
                    this.noticeService.create("Usunięto wydarzenie " + event.getName() + " w którym brałeś udział."),
                    eventUsers);
            removeByIterator(eventUsers);
            removeByIterator(event.getCategories());
            this.requestToJoinService.removeRequestToJoinForDeletedEvent(event);
            this.chatService.prepareChatToRemoveForDeletedEvent(event);
            this.eventRepository.delete(event);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void removeUserParticipationForAllEvents(User user) {
        List<Event> events = this.eventRepository.findAll();
        for (Event event : events) {
            event.getUsers().remove(user);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void removeCategoryForAllEvents(Category category) {
        List<Event> events = this.eventRepository.findAll();
        for (Event event : events) {
            event.getCategories().remove(category);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Event> getAll(int page, int size, String column, Sort.Direction direction, String filter, Boolean activeDate) {
        User user = this.userService.getAuthUser();

        Sort sort = Sort.by(new Sort.Order(direction, column));
        if (filter.equals("")) {
            if (activeDate)
                return this.eventRepository.findAllByDateAfterAndAgeLimitLessThanOrDateAfterAndAgeLimitIsNull(
                        LocalDateTime.now(), this.userService.calculateAge(user.getDateOfBirth()),
                        LocalDateTime.now(), PageRequest.of(page, size, sort));
            else
                return this.eventRepository.findAllByDateBeforeAndAgeLimitLessThanOrDateBeforeAndAgeLimitIsNull(
                        LocalDateTime.now(), this.userService.calculateAge(user.getDateOfBirth()),
                        LocalDateTime.now(), PageRequest.of(page, size, sort));
        } else {
            if (activeDate)
                return this.eventRepository.getAllByNameContainsAndDateAfterAndAgeLimitLessThanOrNameContainsAndDateAfterAndAgeLimitIsNull(
                        filter, LocalDateTime.now(), this.userService.calculateAge(user.getDateOfBirth()),
                        filter, LocalDateTime.now(), PageRequest.of(page, size, sort));
            else
                return this.eventRepository.getAllByNameContainsAndDateBeforeAndAgeLimitLessThanOrNameContainsAndDateBeforeAndAgeLimitIsNull(
                        filter, LocalDateTime.now(), this.userService.calculateAge(user.getDateOfBirth()),
                        filter, LocalDateTime.now(), PageRequest.of(page, size, sort));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Event> getAllForUserCreate(int page, int size, String column, Sort.Direction direction, String filter, Boolean activeDate) {
        User user = this.userService.getAuthUser();
        Sort sort = Sort.by(new Sort.Order(direction, column));
        if (filter.equals("")) {
            if (activeDate)
                return this.eventRepository.getAllByUserAndDateAfter(user, LocalDateTime.now(),
                        PageRequest.of(page, size, sort));
            else
                return this.eventRepository.getAllByUserAndDateBefore(user, LocalDateTime.now(),
                        PageRequest.of(page, size, sort));
        } else {
            if (activeDate)
                return this.eventRepository.getAllByUserAndNameContainsAndDateAfter(user, filter, LocalDateTime.now(),
                        PageRequest.of(page, size, sort));
            else
                return this.eventRepository.getAllByUserAndNameContainsAndDateBefore(user, filter, LocalDateTime.now(),
                        PageRequest.of(page, size, sort));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Event> getAllForUserParticipate(int page, int size, String column, Sort.Direction direction, String filter,
                                                Boolean activeDate) {
        User user = this.userService.getAuthUser();
        Sort sort = Sort.by(new Sort.Order(direction, column));
        if (filter.equals("")) {
            if (activeDate)
                return this.eventRepository.getAllByUsersContainingAndDateAfterAndAgeLimitLessThanOrUsersContainingAndDateAfterAndAgeLimitIsNull(
                        user, LocalDateTime.now(), this.userService.calculateAge(user.getDateOfBirth()),
                        user, LocalDateTime.now(), PageRequest.of(page, size, sort));
            else
                return this.eventRepository.getAllByUsersContainingAndDateBeforeAndAgeLimitLessThanOrUsersContainingAndDateBeforeAndAgeLimitIsNull(
                        user, LocalDateTime.now(), this.userService.calculateAge(user.getDateOfBirth()),
                        user, LocalDateTime.now(), PageRequest.of(page, size, sort));
        } else {
            if (activeDate)
                return this.eventRepository.getAllByUsersContainingAndNameContainsAndDateAfterAndAgeLimitLessThanOrUsersContainingAndNameContainsAndDateAfterAndAgeLimitIsNull(
                        user, filter, LocalDateTime.now(), this.userService.calculateAge(user.getDateOfBirth()),
                        user, filter, LocalDateTime.now(), PageRequest.of(page, size, sort));
            else
                return this.eventRepository.getAllByUsersContainingAndNameContainsAndDateBeforeAndAgeLimitLessThanOrUsersContainingAndNameContainsAndDateBeforeAndAgeLimitIsNull(
                        user, filter, LocalDateTime.now(), this.userService.calculateAge(user.getDateOfBirth()),
                        user, filter, LocalDateTime.now(), PageRequest.of(page, size, sort));
        }
    }

    @Override
    public Boolean existByName(String name) {
        return this.eventRepository.existsByName(name);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void addUserToEventParticipation(Long id, User user) {
        if (isExists(id)) {
            Event event = getById(id);
            if (event.getUsers() == null) {
                event.setUsers(Set.of(user));
            } else {
                this.noticeService.addNoticeToUsers(this.noticeService.create("Użytkownik " + user.getUsername() + " dołączył do Twojego wydarzenia " + event.getName() + "."), Set.of(event.getUser()));
                event.getUsers().add(user);
            }
            this.userService.addChatToUser(user.getId(), event.getChat());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean updateDate(Long id, Event event) {
        if (isExists(id)) {
            User user = this.userService.getAuthUser();
            Event eventById = getById(id);

            if (!(eventById.getUser().equals(user) ||
                    user.getRoles().contains(this.roleService.findByRole(ERole.ROLE_MODERATOR)))) {
                throw new OperationAccessDeniedException("Brak dostępu do edycji eventu o id " + id);
            }

            this.noticeService.addNoticeToUsers(
                    this.noticeService.create("Zaktualizowano date wydarzenia " + eventById.getName() + " w którym bierzesz udział."),
                    eventById.getUsers());

            eventById.setDate(event.getDate());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean updateNumber(Long id, Event event) {
        if (isExists(id)) {
            User user = this.userService.getAuthUser();
            Event eventById = getById(id);

            if (!(eventById.getUser().equals(user) ||
                    user.getRoles().contains(this.roleService.findByRole(ERole.ROLE_MODERATOR)))) {
                throw new OperationAccessDeniedException("Brak dostępu do edycji eventu o id " + id);
            }

            if (event.getMaxNumberOfParticipant() < eventById.getUsers().size()) {
                throw new EventParticipantLimitException("Maksymalna ilość uczestników nie może być mniejsza niż" +
                        " aktualna liczba uczestników.");
            }

            eventById.setMaxNumberOfParticipant(event.getMaxNumberOfParticipant());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean updateCategories(Long id, Event event) {
        if (isExists(id)) {
            User user = this.userService.getAuthUser();
            Event eventById = getById(id);

            if (!(eventById.getUser().equals(user) ||
                    user.getRoles().contains(this.roleService.findByRole(ERole.ROLE_MODERATOR)))) {
                throw new OperationAccessDeniedException("Brak dostępu do edycji eventu o id " + id);
            }

            eventById.setCategories(event.getCategories());
            return true;
        }
        return false;
    }

    public boolean checkAge(User user, Event event) {
        if (event.getAgeLimit() != null) {
            Integer userYears = this.userService.calculateAge(user.getDateOfBirth());
            return (event.getAgeLimit().compareTo(userYears) < 0);
        } else return true;
    }

    public boolean checkPossibilityOfJoining(Event event) {
        if (event.getMaxNumberOfParticipant() == null) {
            return true;
        } else {
            return event.getUsers().size() < event.getMaxNumberOfParticipant() && event.getFreeJoin();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean joinUserToEvent(Long id) {
        User user = this.userService.getAuthUser();

        if (isExists(id)) {
            Event event = getById(id);
            if (checkPossibilityOfJoining(event)) {
                if (checkAge(user, event)) {
                    event.getUsers().add(user);
                    this.userService.addChatToUser(user.getId(), event.getChat());

                    this.noticeService.addNoticeToUsers(this.noticeService.create("Użytkownik " + user.getUsername() + " dołączył do Twojego wydarzenia " + event.getName() + "."), Set.of(event.getUser()));

                    return true;
                } else {
                    throw new AgeLimitException("Prośba użytkownika została odrzuca ze względu na nieodpowiedni wiek.");
                }
            } else {
                throw new EventParticipantLimitException("Przekroczono limit uczestników wydarzenia.");
            }
        } else {
            return false;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean divorceUserFromEvent(Long id) {
        User user = this.userService.getAuthUser();

        if (isExists(id)) {
            Event event = getById(id);
            if (event.getUser().equals(user)) {
                throw new OperationAccessDeniedException("Organizator wydarzenia nie może go opuścić.");
            } else {
                event.getUsers().remove(user);
                this.userService.removeChatFromUser(user.getId(), event.getChat());
                this.noticeService.addNoticeToUsers(this.noticeService.create("Użytkownik " + user.getUsername() + " opuścił Twoje wydarzenie " + event.getName() + "."), Set.of(event.getUser()));

                return true;
            }
        } else return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean removeUserFromEvent(Long id, Long userId) {
        User user = this.userService.getAuthUser();

        if (isExists(id)) {
            Event event = getById(id);
            if (user.getId().equals(userId)) {
                throw new OperationAccessDeniedException("Organizator wydarzenia nie może go opuścić.");
            }
            if (!event.getUser().equals(user)) {
                throw new OperationAccessDeniedException("Tylko organizator może usunąć uczestnika z wydarzenia");
            } else {
                User removeUser = this.userService.getById(userId);
                event.getUsers().remove(removeUser);
                this.userService.removeChatFromUser(userId, event.getChat());

                this.noticeService.addNoticeToUsers(this.noticeService.create("Organizator wydarzenia " + event.getName() + " usunął Cię z listy uczestników"), Set.of(removeUser));

                return true;
            }
        } else return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Event> getAllForAuthUserNonParticipate(int page, int size, String column, Sort.Direction sortDir,
                                                       String filter, boolean activeDate) {
        User user = this.userService.getAuthUser();
        Sort sort = Sort.by(new Sort.Order(sortDir, column));
        if (filter.equals("")) {
            if (activeDate) {
                return this.eventRepository.getAllByUsersNotContainingAndDateAfterAndAgeLimitLessThanOrUsersNotContainingAndDateAfterAndAgeLimitIsNull
                        (user, LocalDateTime.now(), this.userService.calculateAge(user.getDateOfBirth()),
                                user, LocalDateTime.now(), PageRequest.of(page, size, sort));
            } else {
                return this.eventRepository.getAllByUsersNotContainingAndDateBeforeAndAgeLimitLessThanOrUsersNotContainingAndDateBeforeAndAgeLimitIsNull
                        (user, LocalDateTime.now(), this.userService.calculateAge(user.getDateOfBirth()),
                                user, LocalDateTime.now(), PageRequest.of(page, size, sort));
            }
        } else {
            if (activeDate) {
                return this.eventRepository.getAllByUsersNotContainingAndNameContainsAndDateAfterAndAgeLimitLessThanOrUsersNotContainingAndNameContainsAndDateAfterAndAgeLimitIsNull
                        (user, filter, LocalDateTime.now(), this.userService.calculateAge(user.getDateOfBirth()),
                                user, filter, LocalDateTime.now(), PageRequest.of(page, size, sort));
            } else {
                return this.eventRepository.getAllByUsersNotContainingAndNameContainsAndDateBeforeAndAgeLimitLessThanOrUsersNotContainingAndNameContainsAndDateBeforeAndAgeLimitIsNull
                        (user, filter, LocalDateTime.now(), this.userService.calculateAge(user.getDateOfBirth()),
                                user, filter, LocalDateTime.now(), PageRequest.of(page, size, sort));
            }
        }
    }

    private <T> void removeByIterator(Set<T> set) {
        Iterator<T> iterator = set.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }
}
