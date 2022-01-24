package com.example.socialApp.registerToken.model_repo;

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
@Entity(name = "register_token")
@Table(name = "register_token")
public class RegisterToken extends BaseEntity {

    @Column(name = "token", nullable = false, length = 100)
    @NotBlank(message = "Token cannot be blank.")
    @Size(min = 1, max = 100, message = "Token must contain beetwen 1 and 100 characters.")
    private String token;

    @Column(name = "created_date")
    @NotNull(message = "Add date cannot be null.")
    private LocalDateTime createdDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(nullable = false, name = "id_user", referencedColumnName = "id")
    private User user;

}
