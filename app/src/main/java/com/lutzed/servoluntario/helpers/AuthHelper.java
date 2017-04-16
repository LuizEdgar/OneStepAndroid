package com.lutzed.servoluntario.helpers;

import android.content.Context;

import com.lutzed.servoluntario.api.SVApi;
import com.lutzed.servoluntario.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthHelper {

    private static User mUser;
    private static AuthSharedPreferences mAuthSharedPreferences;

    public static User getUser(Context context) {
        AuthSharedPreferences authSharedPreferences = getAuthSharedPreferences(context);

        if (authSharedPreferences.hasUser() && mUser == null) {
            initUser(context);
        }

        return mUser;
    }

    public static boolean hasUser(Context context) {
        return getAuthSharedPreferences(context).hasUser();
    }

    public static AuthSharedPreferences getAuthSharedPreferences(Context context) {
        if (mAuthSharedPreferences == null) {
            mAuthSharedPreferences = new AuthSharedPreferences(context);
        }
        return mAuthSharedPreferences;
    }

    private static void initUser(Context context) {
        mUser = getAuthSharedPreferences(context).getUser();
    }

    public static void setUser(Context context, User user) {
        AuthSharedPreferences authSharedPreferences = getAuthSharedPreferences(context);

        authSharedPreferences.setUser(user);
        authSharedPreferences.setHave(true);

        initUser(context);
    }

    public static void updateUserData(final Context context, final Callback<User> callback) {
        Call<User> meCall = SVApi.getClient().getMe();
        meCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                AuthHelper.setUser(context, response.body());
                if (callback != null) callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (callback != null) callback.onFailure(call, t);
            }
        });
    }

    public static void updateUserData(final Context context) {
        updateUserData(context, null);
    }
}