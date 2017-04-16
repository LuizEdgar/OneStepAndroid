package com.lutzed.servoluntario.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.lutzed.servoluntario.models.User;

/**
 * Created by luizedgar on 2/6/15.
 */
public class AuthSharedPreferences {

    private static final String ACCOUNT_SHARED_PREFERENCES = AuthSharedPreferences.class
            .getSimpleName();

    private static final String USER_HAS = "user_has";
    private static final String USER_JSON = "user_json";

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public AuthSharedPreferences(Context context) {
        this.mSharedPreferences = context.getSharedPreferences(
                ACCOUNT_SHARED_PREFERENCES, Activity.MODE_PRIVATE);
        this.mEditor = mSharedPreferences.edit();
    }

    public void clear() {
        mEditor.clear();
        mEditor.commit();
    }

    public boolean hasUser() {
        return mSharedPreferences.getBoolean(USER_HAS, false);
    }

    public void setHave(boolean has) {
        mEditor.putBoolean(USER_HAS, has);
        mEditor.commit();
    }

    public User getUser() {
        Gson gson = new Gson();
        String json = mSharedPreferences.getString(USER_JSON,
                null);
        try {
            return gson.fromJson(json, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setUser(User user) {
        Gson gson = new Gson();
        mEditor.putString(USER_JSON, gson.toJson(user));
        mEditor.commit();
    }

}
