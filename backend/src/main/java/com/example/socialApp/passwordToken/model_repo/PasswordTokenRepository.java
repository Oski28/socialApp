package com.example.socialApp.passwordToken.model_repo;

import com.example.socialApp.shared.BaseRepository;
import com.example.socialApp.user.model_repo.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordTokenRepository extends BaseRepository<PasswordToken> {

    Optional<PasswordToken> findByToken(String token);

    List<PasswordToken> getAllByExpiryDateBefore(LocalDateTime localDateTime);
}
