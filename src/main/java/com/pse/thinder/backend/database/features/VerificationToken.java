package com.pse.thinder.backend.database.features;

import com.pse.thinder.backend.database.features.account.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity @Table(name = "verficationTokens")
public class VerificationToken {

    private final static int EXPIRATION_DURATION_IN_HOUR = 24; //

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(columnDefinition = "character varying(64) not null")
    private String token;

    @Column(nullable = false)
    private Date expirationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public VerificationToken(){}

    public VerificationToken(String token, User user) {
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
