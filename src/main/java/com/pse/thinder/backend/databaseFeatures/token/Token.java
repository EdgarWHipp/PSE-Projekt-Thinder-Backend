package com.pse.thinder.backend.databaseFeatures.token;

import com.pse.thinder.backend.databaseFeatures.account.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Token {

    private final static int EXPIRATION_DURATION_IN_HOUR = 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Size(min=1, max=64)
    @Column(columnDefinition = "character varying(64) not null")
    private String token;

    @NotNull
    @Column(nullable = false)
    private Date expirationDate;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Token(){}

    public Token(User user, String token) {
        this.token = token;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, EXPIRATION_DURATION_IN_HOUR);
        this.expirationDate = cal.getTime();
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

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
