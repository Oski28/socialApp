package com.example.socialApp.chat.model_repo;

import com.example.socialApp.shared.BaseRepository;
import com.example.socialApp.user.model_repo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends BaseRepository<Chat> {

    Page<Chat> findAllByNameContainsAndUsersContaining(String filter, User user, Pageable pageable);

    Page<Chat> findAllByUsersContaining(User user, Pageable pageable);

    Chat getByUsersContainingAndUsersContainingAndEventIsNull(User user1, User user2);
}
