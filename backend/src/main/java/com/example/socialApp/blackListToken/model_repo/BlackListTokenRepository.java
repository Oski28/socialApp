package com.example.socialApp.blackListToken.model_repo;

import com.example.socialApp.shared.BaseRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BlackListTokenRepository extends BaseRepository<BlackListToken> {

    Boolean existsByToken(String token);

    List<BlackListToken> getAllByExpiryDateBefore(LocalDateTime localDateTime);
}
