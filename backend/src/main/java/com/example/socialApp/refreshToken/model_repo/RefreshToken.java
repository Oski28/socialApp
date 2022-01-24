package com.example.socialApp.refreshToken.model_repo;

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
@Entity(name = "refresh_token")
@Table(name = "refresh_token")
public class RefreshToken extends BaseEntity {

    @Column(name = "token", nullable = false, length = 100, unique = true)
    @NotBlank(message = "Token cannot be blank.")
    @Size(min = 1, max = 100, message = "Token must contain beetwen 1 and 100 characters.")
    private String token;

    @Column(name = "expiry_date", nullable = false)
    @NotNull(message = "Expiry date cannot be null.")
    private LocalDateTime expiryDate;

    /* RELATIONS */
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, name = "id_user", referencedColumnName = "id")
    private User user;
}
