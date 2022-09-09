package com.pse.thinder.backend.databaseFeatures.dto;

/**
 * This class holds all data necessary for the password reset functionality. It's used to receive this
 * data from the frontend.
 */
public class PasswordResetDTO {

    private String token;

    private String newPassword;

    /**
     * Spring boot requires a no args constructor and uses setters to set the properties.
     */
    public PasswordResetDTO(){}

    /**
     *
     * @param token a string identifying a {@link com.pse.thinder.backend.databaseFeatures.token.PasswordResetToken}
     * @param newPassword the new password of the user.
     */
    public PasswordResetDTO(String token, String newPassword){
        this.newPassword = newPassword;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
