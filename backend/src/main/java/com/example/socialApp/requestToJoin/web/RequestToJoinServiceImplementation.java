package com.example.socialApp.requestToJoin.web;

import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.event.web.EventServiceImplementation;
import com.example.socialApp.exceptions.AgeLimitException;
import com.example.socialApp.exceptions.EventParticipantLimitException;
import com.example.socialApp.exceptions.OperationAccessDeniedException;
import com.example.socialApp.notice.web.NoticeServiceImplementation;
import com.example.socialApp.requestToJoin.model_repo.RequestToJoin;
import com.example.socialApp.requestToJoin.model_repo.RequestToJoinRepository;
import com.example.socialApp.requestToJoin.model_repo.Status;
import com.example.socialApp.shared.BaseService;
import com.example.socialApp.user.model_repo.User;
import com.example.socialApp.user.web.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RequestToJoinServiceImplementation implements RequestToJoinService, BaseService<RequestToJoin> {

    private final RequestToJoinRepository requestToJoinRepository;

    private EventServiceImplementation eventService;

    private UserServiceImplementation userService;

    private NoticeServiceImplementation noticeService;

    @Autowired
    public void setEventService(EventServiceImplementation eventService) {
        this.eventService = eventService;
    }

    @Autowired
    public void setUserService(UserServiceImplementation userService) {
        this.userService = userService;
    }

    @Autowired
    public void setNoticeService(NoticeServiceImplementation noticeService) {
        this.noticeService = noticeService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void removeRequestToJoinForDeletedUser(User user) {
        List<RequestToJoin> requestToJoins = this.requestToJoinRepository.getAllByUser(user);
        for (RequestToJoin requestToJoin : requestToJoins) {
            this.requestToJoinRepository.delete(requestToJoin);
            user.getRequestToJoinList().remove(requestToJoin);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void removeRequestToJoinForDeletedEvent(Event event) {
        List<RequestToJoin> requestToJoins = this.requestToJoinRepository.getAllByEvent(event);
        for (RequestToJoin requestToJoin : requestToJoins) {
            this.requestToJoinRepository.delete(requestToJoin);
            event.getRequestToJoinList().remove(requestToJoin);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public Page<RequestToJoin> getAllWaitingForEvent(int page, int size, String column, Sort.Direction direction, Long eventId) {
        Sort sort = Sort.by(new Sort.Order(direction, column));
        Event event = eventService.getById(eventId);

        User user = this.userService.getAuthUser();

        if (event.getUser().equals(user) && !event.getFreeJoin()) {
            return this.requestToJoinRepository.getAllByEventAndStatusIs(event, Status.WAITING, PageRequest.of(page, size, sort));
        } else
            throw new NoSuchElementException("Brak uprawnie?? do rozpatrywania pr????b o do????czenie dla wydarzenia o id "
                    + eventId + " lub wydarzenie oznaczono jako dost??pne dla wszystkich.");

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean existByUserEvent(User user, Long eventId) {
        Event event = this.eventService.getById(eventId);
        return this.requestToJoinRepository.existsByEventAndUser(event, user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public RequestToJoin create(User user, Long eventId) {
        Event event = this.eventService.getById(eventId);

        RequestToJoin requestToJoin = new RequestToJoin();
        requestToJoin.setUser(user);
        requestToJoin.setEvent(event);
        requestToJoin.setStatus(Status.WAITING);
        requestToJoin.setAddDate(LocalDateTime.now());

        this.noticeService.addNoticeToUsers(this.noticeService.create("Wys??ano pro??b?? o do????czenie do Twojego wydarzenia " + event.getName() + "."), Set.of(event.getUser()));

        return this.save(requestToJoin);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void updateAllForEvent(Event event) {
        List<RequestToJoin> requestToJoins = this.requestToJoinRepository.getAllByEvent(event);
        RequestToJoin requestToJoinWithAcceptedStatus = new RequestToJoin();
        requestToJoinWithAcceptedStatus.setStatus(Status.ACCEPTED);
        for (RequestToJoin requestToJoin : requestToJoins) {
            this.update(requestToJoin.getId(), requestToJoinWithAcceptedStatus);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public Page<RequestToJoin> getAllWaitingForUser(int page, int size, String column, Sort.Direction sortDir) {
        Sort sort = Sort.by(new Sort.Order(sortDir, column));
        User user = this.userService.getAuthUser();
        return this.requestToJoinRepository.getAllByUser(user, PageRequest.of(page, size, sort));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean resend(Long id) {
        Optional<RequestToJoin> optional = findById(id);
        if (optional.isPresent()) {
            RequestToJoin requestToJoin = optional.get();
            requestToJoin.setStatus(Status.WAITING);
            requestToJoin.setAddDate(LocalDateTime.now());
            this.noticeService.addNoticeToUsers(this.noticeService.create("Wys??ano pro??b?? o do????czenie do Twojego wydarzenia " + requestToJoin.getEvent().getName() + "."), Set.of(requestToJoin.getEvent().getUser()));

            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RequestToJoin> getAll(int page, int size, String column, Sort.Direction direction) {
        Sort sort = Sort.by(new Sort.Order(direction, column));
        return this.requestToJoinRepository.findAll(PageRequest.of(page, size, sort));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean update(Long id, RequestToJoin entity) {
        Optional<RequestToJoin> optional = findById(id);
        if (optional.isPresent()) {
            RequestToJoin requestToJoin = optional.get();
            User user = this.userService.getAuthUser();
            Event event = this.eventService.getById(requestToJoin.getEvent().getId());

            if (!event.getUser().equals(user)) {
                throw new OperationAccessDeniedException("Brak dost??pu do pr????b o do????czenie.");
            }

            if (entity.getStatus().equals(Status.ACCEPTED)) {
                if (!this.eventService.checkAge(requestToJoin.getUser(), requestToJoin.getEvent())) {
                    requestToJoin.setStatus(Status.REJECTED);
                    this.noticeService.addNoticeToUsers(this.noticeService.create("Twoja pro??ba o do????czenie do wydarzenia " + event.getName()
                            + " zosta??a rozpatrzona negatywnie."), Set.of(requestToJoin.getUser()));
                    throw new AgeLimitException("Pro??ba u??ytkownika zosta??a odrzuca ze wzgl??du na nieodpowiedni wiek.");
                }
                if (this.eventService.checkPossibilityOfJoining(requestToJoin.getEvent())) {
                    requestToJoin.setStatus(entity.getStatus());
                    this.noticeService.addNoticeToUsers(this.noticeService.create("Twoja pro??ba o do????czenie do wydarzenia " + event.getName()
                            + " zosta??a rozpatrzona pozytywnie."), Set.of(requestToJoin.getUser()));
                    this.eventService.addUserToEventParticipation(requestToJoin.getEvent().getId(), requestToJoin.getUser());
                } else {
                    requestToJoin.setStatus(Status.WAITING);
                    throw new EventParticipantLimitException("Przekroczono limit uczestnik??w wydarzenia.");
                }
            } else {
                requestToJoin.setStatus(entity.getStatus());
                this.noticeService.addNoticeToUsers(this.noticeService.create("Twoja pro??ba o do????czenie do wydarzenia " + event.getName()
                        + " zosta??a rozpatrzona negatywnie."), Set.of(requestToJoin.getUser()));
            }

            return true;
        } else return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean delete(Long id) {
        Optional<RequestToJoin> optional = findById(id);
        if (optional.isPresent()) {
            this.requestToJoinRepository.delete(optional.get());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public RequestToJoin save(RequestToJoin entity) {
        return this.requestToJoinRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RequestToJoin> findById(Long id) {
        return this.requestToJoinRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public RequestToJoin getById(Long id) {
        return this.requestToJoinRepository.getById(id);
    }
}
