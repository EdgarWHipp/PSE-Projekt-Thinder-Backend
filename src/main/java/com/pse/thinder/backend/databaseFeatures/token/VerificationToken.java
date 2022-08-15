package com.pse.thinder.backend.databaseFeatures.token;

import com.pse.thinder.backend.databaseFeatures.account.User;

import javax.persistence.*;

@Entity @Table(name = "verficationTokens")
public class VerificationToken extends Token {

    public VerificationToken() {
    }

    public VerificationToken(User user, String token) {
        super(user, token);
    }

}