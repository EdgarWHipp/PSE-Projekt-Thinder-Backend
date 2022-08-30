package com.pse.thinder.backend.databaseFeatures;

public class PasswordResetDTO {

    String token;

    String newPassword;

    public PasswordResetDTO(){}

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
