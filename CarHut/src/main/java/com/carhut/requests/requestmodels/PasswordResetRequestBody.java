package com.carhut.requests.requestmodels;

public class PasswordResetRequestBody {

    private String passwordResetToken;
    private String newPassword;
    private String repeatedNewPassword;
    private String email;

    public PasswordResetRequestBody(String passwordResetToken, String newPassword, String repeatedNewPassword, String email) {
        this.passwordResetToken = passwordResetToken;
        this.newPassword = newPassword;
        this.repeatedNewPassword = repeatedNewPassword;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatedNewPassword() {
        return repeatedNewPassword;
    }

    public void setRepeatedNewPassword(String repeatedNewPassword) {
        this.repeatedNewPassword = repeatedNewPassword;
    }
}
