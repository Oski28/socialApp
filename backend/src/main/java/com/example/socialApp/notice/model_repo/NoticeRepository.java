package com.example.socialApp.notice.model_repo;

import com.example.socialApp.shared.BaseRepository;
import com.example.socialApp.user.model_repo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends BaseRepository<Notice> {

    List<Notice> findAllByUser(User user);

    Page<Notice> findAllByUser(User user, Pageable pageable);

    Page<Notice> findAllByUserAndReceivedFalse(User user, Pageable pageable);
}
