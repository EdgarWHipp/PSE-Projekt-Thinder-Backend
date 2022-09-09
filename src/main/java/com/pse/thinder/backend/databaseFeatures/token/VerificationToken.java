package com.pse.thinder.backend.databaseFeatures.token;

import com.pse.thinder.backend.databaseFeatures.account.User;

import javax.persistence.*;

/**
 * This token is used for the verification of the users.
 */
@Entity @Table(name = "verficationTokens")
public class VerificationToken extends Token {

    /**
     * Spring boot requires a no args constructor and uses setters to set the properties.
     */
    public VerificationToken() {
    }

    /**
     *
     * @param user the user the token is designated for
     * @param token the actual token.
     */
    public VerificationToken(User user, String token) {
        super(user, token);
    }

}