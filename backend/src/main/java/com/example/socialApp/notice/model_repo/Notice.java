package com.example.socialApp.notice.model_repo;

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
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(doNotUseGetters = true)
@NoArgsConstructor
@Entity(name = "notice")
@Table(name = "notice")
public class Notice extends BaseEntity {

    @Column(name = "content", nullable = false, length = 300)
    @NotBlank(message = "Notice content cannot be blank.")
    @Size(min = 1, max = 300, message = "Notice content must contain between 1 and 300 characters.")
    private String content;

    @Column(name = "received", columnDefinition = "boolean default false")
    @Generated(GenerationTime.INSERT)
    private Boolean received;

    /* RELATIONS */
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, name = "id_user", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;
}
