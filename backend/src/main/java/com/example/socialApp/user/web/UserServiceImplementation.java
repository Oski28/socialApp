package com.example.socialApp.user.web;

import com.example.socialApp.chat.model_repo.Chat;
import com.example.socialApp.chat.web.ChatServiceImplementation;
import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.event.web.EventServiceImplementation;
import com.example.socialApp.exceptions.OldPasswordMismatchException;
import com.example.socialApp.exceptions.OperationAccessDeniedException;
import com.example.socialApp.notice.web.NoticeServiceImplementation;
import com.example.socialApp.refreshToken.web.RefreshTokenServiceImplementation;
import com.example.socialApp.requestToJoin.web.RequestToJoinServiceImplementation;
import com.example.socialApp.role.model_repo.ERole;
import com.example.socialApp.role.web.RoleServiceImplementation;
import com.example.socialApp.shared.BaseService;
import com.example.socialApp.shared.EmailSenderService;
import com.example.socialApp.user.dto.UserPasswordDto;
import com.example.socialApp.user.model_repo.User;
import com.example.socialApp.user.model_repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.Set;


@Service
public class UserServiceImplementation implements UserService, BaseService<User> {

    private final UserRepository userRepository;

    private RoleServiceImplementation roleService;

    private RefreshTokenServiceImplementation refreshTokenService;

    private RequestToJoinServiceImplementation requestToJoinService;

    private NoticeServiceImplementation noticeService;

    private EventServiceImplementation eventService;

    private ChatServiceImplementation chatService;

    private EmailSenderService emailSenderService;

    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Autowired
    public void setRoleService(RoleServiceImplementation roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setRefreshTokenService(RefreshTokenServiceImplementation refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @Autowired
    public void setRequestToJoinService(RequestToJoinServiceImplementation requestToJoinService) {
        this.requestToJoinService = requestToJoinService;
    }

    @Autowired
    public void setEmailSenderService(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @Autowired
    public void setNoticeService(NoticeServiceImplementation noticeService) {
        this.noticeService = noticeService;
    }

    @Autowired
    public void setEventService(EventServiceImplementation eventService) {
        this.eventService = eventService;
    }

    @Autowired
    public void setChatService(ChatServiceImplementation chatService) {
        this.chatService = chatService;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean banUser(Long id, LocalDate banDate) {
        if (isExists(id)) {
            User user = getById(id);
            if (user.getRoles().contains(this.roleService.findByRole(ERole.ROLE_MODERATOR)) ||
                    user.getRoles().contains(this.roleService.findByRole(ERole.ROLE_ADMIN))) {
                throw new OperationAccessDeniedException("Nie można zbanować użytkownika o uprawnieniach moderatora.");
            } else {
                user.setBlocked(true);
                user.setBanExpirationDate(LocalDateTime.of(banDate, LocalTime.MIDNIGHT));
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean enableUser(Long id) {
        if (isExists(id)) {
            User user = getById(id);
            user.setEnabled(true);
            user.setDeleteDate(null);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getAllWithFilterByEnabledTrue(int page, int size, String column, String direction, String filter) {
        User user = getAuthUser();
        Sort.Direction sortDir = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(new Sort.Order(sortDir, column));
        return this.userRepository.findAllByFirstnameContainsAndEnabledTrueAndUsernameNotOrLastnameContainsAndEnabledTrueAndUsernameNot(filter, user.getUsername(), filter, user.getUsername(),
                PageRequest.of(page, size, sort));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getAllForEvent(int page, int size, String column, Sort.Direction sortDir, Long eventId) {
        Event event = this.eventService.getById(eventId);
        if (event == null) {
            throw new NullPointerException("Brak takiego wydarzenia");
        }
        Sort sort = Sort.by(new Sort.Order(sortDir, column));
        return this.userRepository.findAllByUserParticipatedEventsContains(event, PageRequest.of(page, size, sort));
    }

    @Override
    public boolean existByUsernameAndEmail(String username, String email) {
        return this.userRepository.existsByUsernameAndEmail(username, email);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void changePassword(User user, String password) {
        user.setPassword(encoder.encode(password));
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void unbanIfBanDateExpired() {
        System.out.println("every day at 00:01 " + LocalTime.now(ZoneOffset.of("Europe/Warsaw")));
        List<User> users = this.userRepository.findAll();
        users.forEach(
                user -> {
                    if (user.getBanExpirationDate().compareTo(LocalDateTime.now()) < 0) {
                        unbanUser(user.getId());

                        SimpleMailMessage mailMessage = new SimpleMailMessage();
                        mailMessage.setTo(user.getEmail());
                        mailMessage.setSubject("Odblokowanie konta!");
                        mailMessage.setFrom("service.rivia@gmail.com");
                        mailMessage.setText("Twoje konto zostało odblokowane.");

                        emailSenderService.sendEmail(mailMessage);
                    }
                }
        );
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean unbanUser(Long id) {
        if (isExists(id)) {
            User user = getById(id);
            user.setBlocked(false);
            user.setBanExpirationDate(null);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean updatePassword(Long id, UserPasswordDto dto) {
        if (isExists(id)) {
            User userById = getById(id);
            if (encoder.matches(dto.getOldPassword(), userById.getPassword())) {
                userById.setPassword(encoder.encode(dto.getNewPassword()));
            } else throw new OldPasswordMismatchException("Podane aktualne hasło nie jest poprawne");
            return true;
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean updateAvatar(Long id, String avatar) {
        if (isExists(id)) {
            User userById = getById(id);
            userById.setAvatar(avatar);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean updateRoles(Long id, User userRole) {
        if (isExists(id)) {
            User userById = getById(id);
            userById.setRoles(userRole.getRoles());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getAll(int page, int size, String column, Sort.Direction direction) {
        Sort sort = Sort.by(new Sort.Order(direction, column));
        return this.userRepository.findAll(PageRequest.of(page, size, sort));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean update(Long id, User entity) {
        if (isExists(id)) {
            User user = getById(id);

            user.setDateOfBirth(entity.getDateOfBirth());
            user.setEmail(entity.getEmail());
            user.setFirstname(entity.getFirstname());
            user.setLastname(entity.getLastname());
            user.setUsername(entity.getUsername());

            return true;
        } else return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean delete(Long id) {
        if (isExists(id)) {
            User user = getById(id);
            user.setDeleteDate(LocalDateTime.now());
            user.setEnabled(false);
            this.noticeService.removeNoticeForDeletedUser(user);
            this.requestToJoinService.removeRequestToJoinForDeletedUser(user);
            this.eventService.removeUserParticipationForAllEvents(user);
            this.chatService.removeUserForAllChats(user);
            this.eventService.removeEventsForDeletedUser(user);
            this.refreshTokenService.deleteByUserId(user.getId());
            return true;
        } else return false;
    }

    @Override
    public User save(User entity) {
        return this.userRepository.save(entity);
    }

    @Override
    public User getById(Long id) {
        return this.userRepository.getById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getAllByEnabledTrue(int page, int size, String column, Sort.Direction direction) {
        User user = getAuthUser();
        Sort sort = Sort.by(new Sort.Order(direction, column));
        return this.userRepository.findAllByEnabledTrueAndUsernameNot(user.getUsername(), PageRequest.of(page, size, sort));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void addChatToUser(Long id, Chat chat) {
        if (isExists(id)) {
            User user = getById(id);
            if (user.getChats() == null) {
                user.setChats(Set.of(chat));
            } else {
                user.getChats().add(chat);
            }
        }
    }

    @Override
    public Integer calculateAge(LocalDate userBirthday) {
        return Period.between(userBirthday, LocalDate.now(ZoneOffset.UTC)).getYears();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void removeChatFromUser(Long id, Chat chat) {
        if (isExists(id)) {
            User user = getById(id);
            user.getChats().remove(chat);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return findByUsername(authentication.getName());
    }
}
