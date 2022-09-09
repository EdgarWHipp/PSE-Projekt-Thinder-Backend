package com.pse.thinder.backend.databaseFeatures.token;

import com.pse.thinder.backend.databaseFeatures.account.User;

import javax.persistence.*;

/**
 * This class holds all necessary data for the password reset token.
 */
@Entity @Table(name="passwordResetTokens")
public class PasswordResetToken extends Token{

    /**
     * Spring boot requires a no args constructor and uses setters to set the properties.
     */
    public PasswordResetToken(){ }

    /**
     *
     * @param user the user the token is designated for
     * @param token the actual token.
     */
    public PasswordResetToken(User user, String token){
        super(user, token);
    }
}