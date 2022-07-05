package com.pse.thinder.backend.databaseFeatures.token;

import com.pse.thinder.backend.databaseFeatures.account.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Table(name="passwordResetTokens")
public class PasswordResetToken extends Token{

    public PasswordResetToken(){ }

    public PasswordResetToken(User user, String token){
        super(user, token);
    }
}