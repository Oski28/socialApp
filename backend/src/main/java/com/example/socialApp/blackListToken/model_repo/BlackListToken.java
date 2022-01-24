package com.example.socialApp.blackListToken.model_repo;

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
@Entity(name = "black_list_token")
@Table(name = "black_list_token")
public class BlackListToken extends BaseEntity {

    @Column(name = "token", nullable = false, length = 200, unique = true)
    @NotBlank(message = "Token cannot be blank.")
    @Size(min = 1, max = 200, message = "Token must contain between 1 and 200 characters.")
    private String token;

    @Column(name = "expiry_date", nullable = false)
    @NotNull(message = "Expiry date cannot be null.")
    private LocalDateTime expiryDate;

    /* RELATIONS */
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "id_user", referencedColumnName = "id")
    private User user;
}
