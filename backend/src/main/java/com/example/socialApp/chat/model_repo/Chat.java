package com.example.socialApp.chat.model_repo;

import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.message.model_repo.Message;
import com.example.socialApp.shared.BaseEntity;
import com.example.socialApp.user.model_repo.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(doNotUseGetters = true)
@NoArgsConstructor
@Entity(name = "chat")
@Table(name = "chat")
public class Chat extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true, length = 50)
    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 3, max = 50, message = "Name must contain between 3 and 50 characters.")
    private String name;

    /* RELATIONS */

    @ManyToMany(mappedBy = "chats", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<User> users;

    @OneToOne(mappedBy = "chat")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Event event;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messages;
}
