package com.example.socialApp.user.model_repo;

import com.example.socialApp.chat.model_repo.Chat;
import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.message.model_repo.Message;
import com.example.socialApp.notice.model_repo.Notice;
import com.example.socialApp.requestToJoin.model_repo.RequestToJoin;
import com.example.socialApp.role.model_repo.Role;
import com.example.socialApp.shared.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(doNotUseGetters = true)
@NoArgsConstructor
@Entity(name = "user")
@Table(name = "user")
public class User extends BaseEntity {

    @Column(name = "avatar")
    @Lob
    private String avatar;

    @Column(name = "firstname", nullable = false, length = 50)
    @NotBlank(message = "Firstname cannot be blank.")
    @Size(min = 3, max = 50, message = "Firstname must contain between 3 and 50 characters.")
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 50)
    @NotBlank(message = "Lastname cannot be blank.")
    @Size(min = 3, max = 50, message = "Lastname must contain between 3 and 50 characters.")
    private String lastname;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    @NotBlank(message = "Username cannot be blank.")
    @Size(min = 3, max = 50, message = "Username must contain between 3 and 50 characters.")
    private String username;

    @Column(name = "email", unique = true, nullable = false, length = 50)
    @Email(message = "Email must be correct.")
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "date_of_birth", nullable = false)
    @NotNull(message = "Date of birth cannot be null.")
    private LocalDate dateOfBirth;

    @Column(name = "add_date", nullable = false)
    @NotNull(message = "Add date cannot be null.")
    private LocalDateTime addDate;

    @Column(name = "delete_date")
    private LocalDateTime deleteDate;

    @Column(name = "enabled", columnDefinition = "boolean default false")
    @Generated(GenerationTime.INSERT)
    private Boolean enabled;

    @Column(name = "blocked", columnDefinition = "boolean default false")
    @Generated(GenerationTime.INSERT)
    private Boolean blocked;

    @Column(name = "ban_expiration_date")
    private LocalDateTime banExpirationDate;

    /* RELATIONS */

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "id_user", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "id_role", referencedColumnName = "id")},
            indexes = {@Index(name = "user_role_index", columnList = "id_user,id_role", unique = true)})
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(name = "user_chat",
            joinColumns = {@JoinColumn(name = "id_user", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "id_chat", referencedColumnName = "id")},
            indexes = {@Index(name = "user_chat_index", columnList = "id_user,id_chat", unique = true)})
    private Set<Chat> chats;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Notice> notices;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Event> userCreatedEvents;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Event> userParticipatedEvents;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Message> messages;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<RequestToJoin> requestToJoinList;
}
