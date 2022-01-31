package com.example.socialApp.notice.web;

import com.example.socialApp.exceptions.OperationAccessDeniedException;
import com.example.socialApp.notice.model_repo.Notice;
import com.example.socialApp.notice.model_repo.NoticeRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NoticeServiceImplementation implements NoticeService, BaseService<Notice> {

    private final NoticeRepository noticeRepository;

    private UserServiceImplementation userService;

    @Autowired
    public void setUserService(UserServiceImplementation userService) {
        this.userService = userService;
    }

    @Override
    public Page<Notice> getAll(int page, int size, String column, Sort.Direction direction) {
        Sort sort = Sort.by(new Sort.Order(direction, column));
        return this.noticeRepository.findAll(PageRequest.of(page, size, sort));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean update(Long id, Notice entity) {
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean delete(Long id) {
        Optional<Notice> optionalNotice = findById(id);
        if (optionalNotice.isPresent()){
            User user = this.userService.getAuthUser();
            Notice notice = optionalNotice.get();

            if (!notice.getUser().equals(user)) {
                throw new OperationAccessDeniedException("Brak dostępu do usuwania powiadomienia o id " + id);
            }
            this.noticeRepository.delete(optionalNotice.get());
            return true;
        } else{
            return false;
        }
    }

    @Override
    public Notice save(Notice entity) {
        return this.noticeRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Notice> findById(Long id) {
        return this.noticeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Notice getById(Long id) {
        return this.noticeRepository.getById(id);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void removeNoticeForDeletedUser(User user) {
        List<Notice> notices = this.noticeRepository.findAllByUser(user);
        for (Notice notice : notices) {
            this.noticeRepository.delete(notice);
            user.getNotices().remove(notice);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public Page<Notice> getAllNotReceivedForUser(int page, int size, String column, Sort.Direction sortDir) {
        User user = this.userService.getAuthUser();
        Sort sort = Sort.by(new Sort.Order(sortDir, column));
        return this.noticeRepository.findAllByUserAndReceivedFalse(user, PageRequest.of(page, size, sort));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notice> getAllForUser(int page, int size, String column, Sort.Direction sortDir) {
        User user = this.userService.getAuthUser();
        Sort sort = Sort.by(new Sort.Order(sortDir, column));
        return this.noticeRepository.findAllByUser(user, PageRequest.of(page, size, sort));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void received(Long id) {
        Notice notice = getById(id);
        notice.setReceived(true);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void addNoticeToUsers(Notice notice, Set<User> users) {
        for (User user : users) {
            Notice newNotice = new Notice();
            newNotice.setContent(notice.getContent());
            newNotice.setReceived(notice.getReceived());
            newNotice.setUser(user);
            user.getNotices().add(newNotice);
            this.save(newNotice);
        }
    }

    @Override
    public Notice create(String content) {
        Notice notice = new Notice();
        notice.setReceived(false);
        notice.setContent(content);
        return notice;
    }
}
