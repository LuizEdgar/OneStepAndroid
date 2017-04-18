package com.lutzed.servoluntario.api.requests;

import com.google.gson.annotations.Expose;

/**
 * Created by luizedgar on 2/6/15.
 */
public class RecoveryPasswordRequest {

    @Expose
    private String email;

    public RecoveryPasswordRequest(String email) {
        this.email = email;
    }

}
