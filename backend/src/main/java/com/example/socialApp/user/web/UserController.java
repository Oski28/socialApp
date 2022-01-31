package com.example.socialApp.user.web;

import com.example.socialApp.exceptions.DuplicateObjectException;
import com.example.socialApp.payload.request.BanRequest;
import com.example.socialApp.payload.request.SignupRequest;
import com.example.socialApp.registerToken.model_repo.RegisterToken;
import com.example.socialApp.registerToken.web.RegisterTokenService;
import com.example.socialApp.role.model_repo.ERole;
import com.example.socialApp.role.web.RoleService;
import com.example.socialApp.shared.BaseController;
import com.example.socialApp.shared.BaseService;
import com.example.socialApp.shared.EmailSenderService;
import com.example.socialApp.user.converter.*;
import com.example.socialApp.user.dto.*;
import com.example.socialApp.user.model_repo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalTime;

@RestController
@RequestMapping(path = "/api/users")
@CrossOrigin
public class UserController extends BaseController<User> {

    private final UserServiceImplementation userService;

    private final UserConverter userConverter;

    private final RegisterTokenService registerTokenService;

    private final EmailSenderService emailSenderService;

    private final UserShowWithEmailConverter userShowWithEmailConverter;

    private final UserShowWithoutEmailConverter userShowWithoutEmailConverter;

    private final RoleService roleService;

    private final UserRoleConverter userRoleConverter;

    private final UserUpdateConverter userUpdateConverter;

    private final UserAvatarConverter userAvatarConverter;

    @Autowired
    public UserController(BaseService<User> service, UserServiceImplementation userService,
                          UserConverter userConverter, RegisterTokenService registerTokenService,
                          EmailSenderService emailSenderService, UserShowWithEmailConverter userShowWithEmailConverter,
                          UserShowWithoutEmailConverter userShowWithoutEmailConverter, RoleService roleService, UserRoleConverter userRoleConverter,
                          UserUpdateConverter userUpdateConverter, UserAvatarConverter userAvatarConverter) {
        super(service);
        this.userService = userService;
        this.userConverter = userConverter;
        this.registerTokenService = registerTokenService;
        this.emailSenderService = emailSenderService;
        this.userShowWithEmailConverter = userShowWithEmailConverter;
        this.userShowWithoutEmailConverter = userShowWithoutEmailConverter;
        this.roleService = roleService;
        this.userRoleConverter = userRoleConverter;
        this.userUpdateConverter = userUpdateConverter;
        this.userAvatarConverter = userAvatarConverter;
    }

    /* GET */

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserShowDto> getOne(@PathVariable final Long id) {
        User user = userService.getAuthUser();
        if (user.getId().equals(id) || user.getRoles().contains(this.roleService.findByRole(ERole.ROLE_MODERATOR))) {
            return super.getOne(id, this.userShowWithEmailConverter.toDto());
        } else return super.getOne(id, this.userShowWithoutEmailConverter.toDto());
    }

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<UserShowDto>> getAll(@RequestParam(defaultValue = "0") final int page,
                                                    @RequestParam(defaultValue = "20") final int size,
                                                    @RequestParam(defaultValue = "id") final String column,
                                                    @RequestParam(defaultValue = "ASC") final String direction,
                                                    @RequestParam(defaultValue = "") final String filter) {
        if (filter.equals("")) {
            Sort.Direction sortDir = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Page<User> userPage = this.userService.getAllByEnabledTrue(page, size, column, sortDir);
            return ResponseEntity.ok(userPage.map(this.userShowWithoutEmailConverter.toDto()));
        } else {
            return ResponseEntity.ok(this.userService.getAllWithFilterByEnabledTrue(page, size, column, direction, filter)
                    .map(this.userShowWithoutEmailConverter.toDto()));
        }
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<UserShowDto>> getAllForEvent(@PathVariable final Long eventId,
                                                            @RequestParam(defaultValue = "0") final int page,
                                                            @RequestParam(defaultValue = "20") final int size,
                                                            @RequestParam(defaultValue = "id") final String column,
                                                            @RequestParam(defaultValue = "ASC") final String direction) {
        Sort.Direction sortDir = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Page<User> userPage = this.userService.getAllForEvent(page, size, column, sortDir, eventId);
        return ResponseEntity.ok(userPage.map(this.userShowWithoutEmailConverter.toDto()));
    }

    @GetMapping("/{id}/avatar")
    @PreAuthorize("#id == authentication.principal.id and hasRole('USER')")
    public ResponseEntity<UserAvatarDto> getAvatar(@PathVariable final Long id) {
        return super.getOne(id, this.userAvatarConverter.toDto());
    }

    /* POST */

    @PostMapping(value = "")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody @Valid SignupRequest signupRequest) {

        String accountType;

        if (userService.existByEmail(signupRequest.getEmail())) {
            throw new DuplicateObjectException("Użytkownik o adresie email "
                    + signupRequest.getEmail() + " istnieje w aplikacji");
        }

        if (userService.existByUsername(signupRequest.getUsername())) {
            throw new DuplicateObjectException("Użytkownik o adresie nicku"
                    + signupRequest.getUsername() + " istnieje w aplikacji");
        }

        User savedUser = userService.save(userConverter.toEntity().apply(signupRequest));

        RegisterToken registerToken = registerTokenService.create(savedUser);

        if (savedUser.getRoles().contains(roleService.findByRole(ERole.ROLE_ADMIN))) {
            accountType = "Administratora";
        } else {
            accountType = "Moderatora";
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(savedUser.getEmail());
        mailMessage.setSubject("Potwierdzenie utworzenia konta!");
        mailMessage.setFrom("service.rivia@gmail.com");
        mailMessage.setText("Aby uaktywnić konto " + accountType + ", klikij w poniższy link:\n"
                + "http://localhost:4200/signin?token=" + registerToken.getToken());

        emailSenderService.sendEmail(mailMessage);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    /* PUT */

    @PutMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id and hasRole('USER')")
    public ResponseEntity<?> update(@PathVariable final Long id, @RequestBody @Valid final UserUpdateDto dto) {
        if (this.userService.existByEmail(dto.getEmail()) && !this.userService.getById(id).getEmail().equals(dto.getEmail())) {
            throw new DuplicateObjectException("Użytkownik o adresie email "
                    + dto.getEmail() + " istnieje w aplikacji");
        }
        if (this.userService.existByUsername(dto.getUsername()) && !this.userService.getById(id).getUsername().equals(dto.getUsername())) {
            throw new DuplicateObjectException("Użytkownik o nicku "
                    + dto.getUsername() + " istnieje w aplikacji");
        }
        return super.update(id, this.userUpdateConverter.toEntity().apply(dto));
    }

    /* PATCH */

    @PatchMapping("/{id}/avatar")
    @PreAuthorize("#id == authentication.principal.id and hasRole('USER')")
    public ResponseEntity<Void> updateAvatar(@PathVariable final Long id, @RequestBody UserAvatarDto dto) {
        if (this.userService.updateAvatar(id, dto.getAvatar())) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/password")
    @PreAuthorize("#id == authentication.principal.id and hasRole('USER')")
    public ResponseEntity<Void> updatePassword(@PathVariable final Long id, @RequestBody @Valid final UserPasswordDto dto) {
        if (this.userService.updatePassword(id, dto)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateRoles(@PathVariable final Long id, @RequestBody final UserRoleDto dto) {
        if (this.userService.updateRoles(id, this.userRoleConverter.toEntity().apply(dto)))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/ban")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Void> banUser(@PathVariable final Long id, @RequestBody @Valid final BanRequest banRequest) {
        if (this.userService.banUser(id, banRequest.getBanExpirationDate())) {

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userService.getById(id).getEmail());
            mailMessage.setSubject("Czasowe zablokowanie konta!");
            mailMessage.setFrom("service.rivia@gmail.com");
            mailMessage.setText("W związku naruszeniem regulaminu serwisu, Twoje konto zostało czasowo zablokowane." +
                    " Odzyskasz dostęp do konta " + banRequest.getBanExpirationDate() + " " + LocalTime.MIDNIGHT + ".");

            emailSenderService.sendEmail(mailMessage);

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/unban")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Void> unbanUser(@PathVariable final Long id) {
        if (this.userService.unbanUser(id)) {

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userService.getById(id).getEmail());
            mailMessage.setSubject("Odblokowanie konta!");
            mailMessage.setFrom("service.rivia@gmail.com");
            mailMessage.setText("Twoje konto zostało odblokowane.");

            emailSenderService.sendEmail(mailMessage);

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /* DELETE */

    @DeleteMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id and hasRole('USER')")
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        if (this.userService.delete(id)) {

            User user = userService.getById(id);

            RegisterToken registerToken = this.registerTokenService.findByUser(user);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Usunięcie konta!");
            mailMessage.setFrom("service.rivia@gmail.com");
            mailMessage.setText("Twoje konto zostało usunięte. Aby je odzyskać kliknij w poniższy link: \n"
                    + "http://localhost:4200/signin?token=" + registerToken.getToken());

            emailSenderService.sendEmail(mailMessage);

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
