package com.pse.thinder.backend.database.features;

import com.pse.thinder.backend.database.features.account.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Table(name = "verficationTokens")
public class VerificationToken {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(columnDefinition = "character varying(64) not null")
    private String token;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public VerificationToken(){}

    public VerificationToken(String token, LocalDateTime expirationDate, User user) {
        this.token = token;
        this.expirationDate = expirationDate;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
