package com.example.socialApp.event.model_repo;

import com.example.socialApp.category.model_repo.Category;
import com.example.socialApp.chat.model_repo.Chat;
import com.example.socialApp.requestToJoin.model_repo.RequestToJoin;
import com.example.socialApp.shared.BaseEntity;
import com.example.socialApp.user.model_repo.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(doNotUseGetters = true)
@NoArgsConstructor
@Entity(name = "event")
@Table(name = "event")
public class Event extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true, length = 50)
    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 3, max = 50, message = "Name must contain between 3 and 50 characters.")
    private String name;

    @Column(name = "description", nullable = false, length = 300)
    @NotBlank(message = "Description cannot be blank.")
    @Size(min = 1, max = 300, message = "Description must contain between 1 and 300 characters.")
    private String description;

    @Column(name = "age_limit")
    @Min(value = 12, message = "Age limit must be more than 11")
    @Max(value = 18, message = "Age limit must be more than 19")
    private Integer ageLimit;

    @Column(name = "max_number_of_participant")
    @Min(value = 2, message = "Max number of participant must be more than 1")
    @Max(value = 149, message = "Max number of participant must be less than 150")
    private Integer maxNumberOfParticipant;

    @Column(name = "location", nullable = false, length = 200)
    @NotBlank(message = "Location cannot be blank.")
    @Size(min = 1, max = 200, message = "Location must contain between 1 and 200 characters.")
    private String location;

    @Column(name = "date", nullable = false)
    @FutureOrPresent(message = "Date must be future or present date.")
    @NotNull(message = "Date cannot be null.")
    private LocalDateTime date;

    @Column(name = "free_join")
    private Boolean freeJoin;

    /* RELATIONS */

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(nullable = false, name = "organizer_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "event_category",
            joinColumns = {@JoinColumn(name = "id_event", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "id_category", referencedColumnName = "id")},
            indexes = {@Index(name = "event_category_index", columnList = "id_event,id_category", unique = true)})
    private Set<Category> categories;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "participant_event_list",
            joinColumns = {@JoinColumn(name = "id_event", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "id_user", referencedColumnName = "id")},
            indexes = {@Index(name = "event_user_index", columnList = "id_event,id_user", unique = true)})
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> users;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Chat chat;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<RequestToJoin> requestToJoinList;
}
