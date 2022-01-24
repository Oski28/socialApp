package com.example.socialApp.registerToken.model_repo;

import com.example.socialApp.shared.BaseRepository;
import com.example.socialApp.user.model_repo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterTokenRepository extends BaseRepository<RegisterToken> {

    RegisterToken findByToken(String token);

    RegisterToken findByUser(User user);
}
