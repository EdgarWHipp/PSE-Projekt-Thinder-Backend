package com.pse.thinder.backend.database.features;

import com.pse.thinder.backend.database.features.account.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class PasswordResetToken {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String token;

    private LocalDateTime expirationDate;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user")
    private User user;

    public PasswordResetToken(){}

    public PasswordResetToken(String token, LocalDateTime expirationDate, User user) {
        this.token = token;
        this.expirationDate = expirationDate;
        this.user = user;
    }
}