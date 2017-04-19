package com.lutzed.servoluntario.login;

import android.text.TextUtils;

import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.api.requests.SignInRequest;
import com.lutzed.servoluntario.models.User;
import com.lutzed.servoluntario.util.Snippets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private final LoginContract.View mView;
    private final Api.ApiClient mApiClient;

    public LoginPresenter(LoginFragment loginFragment, Api.ApiClient apiClient) {
        mView = loginFragment;
        mApiClient = apiClient;
        mView.setPresenter(this);
    }

    @Override
    public void attemptEmailLogin(String email, String password) {
        mView.resetLoginErrors();

        boolean cancel = false;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mView.showEmailRequiredError();
            cancel = true;
        } else if (!Snippets.isEmailValid(email)) {
            mView.showInvalidEmailError();
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mView.showPasswordRequiredError();
            if (!cancel) mView.setFocusPasswordField();
            cancel = true;
        } else if (!Snippets.isPasswordValid(password)) {
            mView.showInvalidPasswordError();
            if (!cancel) mView.setFocusPasswordField();
            cancel = true;
        }

        if (!cancel) {
            mView.setLoadingIndicator(true);
            mApiClient.signIn(new SignInRequest(email, password)).enqueue(
                    new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            mView.setLoadingIndicator(false);
                            switch (response.code()) {
                                case 200:
                                    mView.navigateToMain();
                                    break;
                                case 401:
                                    mView.showInvalidPasswordError();
                                    break;
                                default:
                                    mView.showLoginDefaultError();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            mView.setLoadingIndicator(false);
                            mView.showLoginDefaultError();
                        }
                    }
            );
        }

    }

    @Override
    public void start() {

    }
}
