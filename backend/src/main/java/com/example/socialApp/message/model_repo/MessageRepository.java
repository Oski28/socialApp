package com.example.socialApp.message.model_repo;

import com.example.socialApp.chat.model_repo.Chat;
import com.example.socialApp.shared.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends BaseRepository<Message> {

    List<Message> findAllByChat(Chat chat);

    Page<Message> getAllByChat(Chat chat, Pageable pageable);
}
