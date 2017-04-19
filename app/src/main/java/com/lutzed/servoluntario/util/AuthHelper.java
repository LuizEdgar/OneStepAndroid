package com.lutzed.servoluntario.util;

import android.content.Context;

import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthHelper {

    private static AuthHelper mAuthHelper;
    private static User mUser;
    private final AuthSharedPreferences mAuthSharedPreferences;

    private AuthHelper(AuthSharedPreferences mAuthSharedPreferences) {
        this.mAuthSharedPreferences = mAuthSharedPreferences;
    }

    public static AuthHelper getInstance(Context context) {
        if (mAuthHelper == null) {
            mAuthHelper = new AuthHelper(new AuthSharedPreferences(context));
        }
        return mAuthHelper;
    }

    public User getUser() {
        if (mAuthSharedPreferences.hasUser() && mUser == null) {
            initUser();
        }

        return mUser;
    }

    public boolean hasUser() {
        return mAuthSharedPreferences.hasUser();
    }

    private void initUser() {
        mUser = mAuthSharedPreferences.getUser();
    }

    public void setUser(User user) {
        mAuthSharedPreferences.setUser(user);
        mAuthSharedPreferences.setHave(true);
        initUser();
    }

    public void updateUserData(final Callback<User> callback) {
        Call<User> meCall = Api.getClient(mUser).getMe();
        meCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                setUser(response.body());
                if (callback != null) callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (callback != null) callback.onFailure(call, t);
            }
        });
    }

    public void updateUserData() {
        updateUserData(null);
    }
}