package com.lutzed.servoluntario.api.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by luizedgar on 2/6/15.
 */
public class SignInRequest {

    @Expose
    private String email;

    @Expose
    private String password;

    public SignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
