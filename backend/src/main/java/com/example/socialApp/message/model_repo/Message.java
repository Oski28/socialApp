package com.example.socialApp.message.model_repo;

import com.example.socialApp.chat.model_repo.Chat;
import com.example.socialApp.shared.BaseEntity;
import com.example.socialApp.user.model_repo.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(doNotUseGetters = true)
@NoArgsConstructor
@Entity(name = "message")
@Table(name = "message")
public class Message extends BaseEntity {

    @Column(name = "content", nullable = false, length = 300)
    @NotBlank(message = "Message content cannot be blank.")
    @Size(min = 1, max = 300, message = "Message content must contain between 1 and 300 characters.")
    private String content;

    @Column(name = "file")
    @Lob
    private String file;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "write_date")
    @NotNull(message = "Write date cannot be null.")
    private LocalDateTime writeDate;

    @Column(name = "delete_date")
    private LocalDateTime deleteDate;

    /* RELATIONS */
    @ManyToOne(targetEntity = Chat.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Chat chat;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "sender_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    
}
