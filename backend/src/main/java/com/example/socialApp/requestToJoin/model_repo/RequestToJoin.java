package com.example.socialApp.requestToJoin.model_repo;

import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.shared.BaseEntity;
import com.example.socialApp.user.model_repo.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(doNotUseGetters = true)
@NoArgsConstructor
@Entity(name = "request_to_join")
@Table(name = "request_to_join")
public class RequestToJoin extends BaseEntity {

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, name = "user_joining_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, name = "event_id", referencedColumnName = "id")
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private Status status;

    @Column(name = "add_date", nullable = false)
    @NotNull(message = "Add date cannot be null.")
    private LocalDateTime addDate;
}
