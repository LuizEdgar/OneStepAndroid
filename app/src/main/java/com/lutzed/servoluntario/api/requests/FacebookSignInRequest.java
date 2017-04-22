package com.lutzed.servoluntario.api.requests;

import com.google.gson.annotations.Expose;

/**
 * Created by luizedgar on 2/6/15.
 */
public class FacebookSignInRequest {

    @Expose
    private String facebook_token;

    public FacebookSignInRequest(String facebook_token) {
        this.facebook_token = facebook_token;
    }
}
