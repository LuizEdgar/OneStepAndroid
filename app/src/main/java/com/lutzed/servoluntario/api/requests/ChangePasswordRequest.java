package com.lutzed.servoluntario.api.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static android.R.attr.password;

/**
 * Created by luizedgar on 2/6/15.
 */
public class ChangePasswordRequest {

    @Expose
    @SerializedName("new_password")
    private String newPassword;

    @Expose
    @SerializedName("current_password")
    private String currentPassword;

    public ChangePasswordRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
