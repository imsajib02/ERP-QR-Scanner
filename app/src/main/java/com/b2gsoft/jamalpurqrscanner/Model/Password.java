package com.b2gsoft.jamalpurqrscanner.Model;

import com.google.gson.annotations.SerializedName;

public class Password {

    @SerializedName("current_password")
    private String currentPassword;

    @SerializedName("password")
    private String newPassword;

    @SerializedName("password_confirmation")
    private String confirmPassword;

    public Password(String currentPassword, String newPassword, String confirmPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
