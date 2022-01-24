package com.example.socialApp.user.web;

import com.example.socialApp.chat.model_repo.Chat;
import com.example.socialApp.user.dto.UserPasswordDto;
import com.example.socialApp.user.model_repo.User;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

public interface UserService {

    Page<User> getAllByEnabledTrue(int page, int size, String column, Sort.Direction direction);

    Page<User> getAllWithFilterByEnabledTrue(int page, int size, String column, String direction, String filter);

    User findByUsername(String username);


    boolean existByUsername(String username);

    boolean existByEmail(String email);

    boolean banUser(Long id, LocalDate banDate);

    boolean enableUser(Long id);

    void unbanIfBanDateExpired();

    boolean unbanUser(Long id);

    boolean updatePassword(Long id, UserPasswordDto dto);

    boolean updateRoles(Long id, User userRole);

    void addChatToUser(Long id, Chat chat);

    Integer calculateAge(LocalDate userBirthday);

    void removeChatFromUser(Long id, Chat chat);

    User getAuthUser();

    boolean updateAvatar(Long id, String avatar) throws FileUploadException;

    Page<User> getAllForEvent(int page, int size, String column, Sort.Direction sortDir, Long eventId);

    boolean existByUsernameAndEmail(String username, String email);

    void changePassword(User user, String password);
}
