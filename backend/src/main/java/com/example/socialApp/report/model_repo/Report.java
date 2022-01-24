package com.example.socialApp.report.model_repo;

import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.shared.BaseEntity;
import com.example.socialApp.user.model_repo.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(doNotUseGetters = true)
@NoArgsConstructor
@Entity(name = "report")
@Table(name = "report")
public class Report extends BaseEntity {

    @Column(name = "add_date", nullable = false)
    @NotNull(message = "Add date cannot be null.")
    private LocalDateTime addDate;

    @Column(name = "reason", nullable = false, length = 300)
    @NotBlank(message = "Reason cannot be blank.")
    @Size(min = 1, max = 300, message = "Reason must contain between 1 and 300 characters.")
    private String reason;

    @Column(name = "received", columnDefinition = "boolean default false")
    @Generated(GenerationTime.INSERT)
    private Boolean received;

    /* RELATIONS */
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, name = "id_reported_user", referencedColumnName = "id")
    private User user;

    /* RELATIONS */
    @ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reported_event", referencedColumnName = "id")
    private Event event;
}
