package com.example.socialApp.security;

import com.example.socialApp.blackListToken.web.BlackListTokenService;
import com.example.socialApp.exceptions.DuplicateObjectException;
import com.example.socialApp.exceptions.TokenException;
import com.example.socialApp.passwordToken.dto.PasswordDto;
import com.example.socialApp.passwordToken.model_repo.PasswordToken;
import com.example.socialApp.passwordToken.web.PasswordTokenService;
import com.example.socialApp.payload.request.*;
import com.example.socialApp.payload.response.JwtResponse;
import com.example.socialApp.payload.response.MessageResponse;
import com.example.socialApp.payload.response.TokenRefreshResponse;
import com.example.socialApp.refreshToken.model_repo.RefreshToken;
import com.example.socialApp.registerToken.model_repo.RegisterToken;
import com.example.socialApp.registerToken.web.RegisterTokenService;
import com.example.socialApp.security.jwt.JwtUtils;
import com.example.socialApp.refreshToken.web.RefreshTokenServiceImplementation;
import com.example.socialApp.security.services.UserDetailsImplementation;
import com.example.socialApp.shared.BaseService;
import com.example.socialApp.shared.EmailSenderService;
import com.example.socialApp.user.model_repo.User;
import com.example.socialApp.user.converter.UserConverter;
import com.example.socialApp.user.web.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final BaseService<User> userBaseService;
    private final UserServiceImplementation userService;
    private final JwtUtils jwtUtils;
    private final RefreshTokenServiceImplementation refreshTokenService;
    private final UserConverter userConverter;
    private final RegisterTokenService registerTokenService;
    private final EmailSenderService emailSenderService;
    private final BlackListTokenService blackListTokenService;
    private final PasswordTokenService passwordTokenService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, BaseService<User> userBaseService,
                          UserServiceImplementation userService, JwtUtils jwtUtils,
                          RefreshTokenServiceImplementation refreshTokenServiceImplementation, UserConverter userConverter,
                          RegisterTokenService registerTokenService, EmailSenderService emailSenderService,
                          BlackListTokenService blackListTokenService, PasswordTokenService passwordTokenService) {
        this.authenticationManager = authenticationManager;
        this.userBaseService = userBaseService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenServiceImplementation;
        this.userConverter = userConverter;
        this.registerTokenService = registerTokenService;
        this.emailSenderService = emailSenderService;
        this.blackListTokenService = blackListTokenService;
        this.passwordTokenService = passwordTokenService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getFirstname(), userDetails.getLastname(),
                userDetails.getEmail(), roles));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest refreshRequest) {
        String reguestRefreshToken = refreshRequest.getRefreshToken();

        return refreshTokenService.findByToken(reguestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, reguestRefreshToken));
                })
                .orElseThrow(() -> new TokenException(reguestRefreshToken,
                        "Refresh token wygasł."));
    }

    @PostMapping("/passwordToken")
    public ResponseEntity<?> passwordToken(@Valid @RequestBody PasswordTokenRequest passwordTokenRequest) {
        if (passwordTokenService.verifyRequest(passwordTokenRequest)) {
            PasswordToken passwordToken = passwordTokenService.createToken(passwordTokenRequest.getUsername());

            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setTo(passwordTokenRequest.getEmail());
            mailMessage.setSubject("Zmiana hasła!");
            mailMessage.setFrom("service.rivia@gmail.com");
            mailMessage.setText("W celu zmiany hasła należy skorzystać z linku:\n"
                    + "http://localhost:4200/password?token=" + passwordToken.getToken());

            emailSenderService.sendEmail(mailMessage);

            return ResponseEntity.ok(new MessageResponse("Na adres email przesłano link do stworzenia nowego hasła. Link aktywny przez 15 minut."));
        } else {
            throw new TokenException(passwordTokenRequest.getUsername(), "Niepoprawne dane użytkownika");
        }
    }

    @PostMapping("/passwordChange")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordDto dto) {
        PasswordToken passwordToken = passwordTokenService.findByToken(dto.getToken());
        if (passwordToken == null) {
            throw new NullPointerException("Niepoprawny token. Podaj poprawny token aby zmienić hasło.");
        }
        if (this.passwordTokenService.isExpired(passwordToken)) {
            throw new TokenException(passwordToken.getToken(), "Token wygasł. Proszę użyć nowego tokenu");
        }
        userService.changePassword(passwordToken.getUser(), dto.getPassword());
        passwordTokenService.removeToken(passwordToken);
        return ResponseEntity.ok(new MessageResponse("Hasło zostało zmienione."));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody @Valid LogoutRequest logoutRequest) {
        refreshTokenService.deleteByUserId(logoutRequest.getUserId());
        blackListTokenService.create(logoutRequest.getToken(), userService.getById(logoutRequest.getUserId()));
        return ResponseEntity.ok(new MessageResponse("Użytkownik poprawnie wylogowany."));
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<?> register(@RequestBody @Valid SignupRequest signupRequest) {

        if (userService.existByEmail(signupRequest.getEmail())) {
            throw new DuplicateObjectException("Użytkownik o adresie email " + signupRequest.getEmail() + " istnieje w aplikacji.");
        }

        if (userService.existByUsername(signupRequest.getUsername())) {
            throw new DuplicateObjectException("Użytkownik o nicku " + signupRequest.getUsername() + " istnieje w aplikacji.");
        }
        User savedUser = userBaseService.save(userConverter.toEntity().apply(signupRequest));

        RegisterToken registerToken = registerTokenService.create(savedUser);

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(savedUser.getEmail());
        mailMessage.setSubject("Potwierdzenie rejestracji!");
        mailMessage.setFrom("service.rivia@gmail.com");
        mailMessage.setText("Aby potwierdzić rejestrację w serwisie Rivia, klikij w poniższy link:\n"
                + "http://localhost:4200/signin?token=" + registerToken.getToken());

        emailSenderService.sendEmail(mailMessage);

        return ResponseEntity.ok(new MessageResponse("Użytkownik poprawnie zarejestrowany."));
    }

    @GetMapping(value = "/confirm-account")
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token") String token) {
        RegisterToken registerToken = registerTokenService.findByToken(token);
        if (registerToken != null) {
            userService.enableUser(registerToken.getUser().getId());
            return ResponseEntity.ok(new MessageResponse("Twoje konto zostało odblokowane."));
        } else {
            throw new NullPointerException("Niepoprawny token. Podaj poprawny token aby odblokować konto lub ponownie zarejestruj się w serwisie.");
        }
    }
}
