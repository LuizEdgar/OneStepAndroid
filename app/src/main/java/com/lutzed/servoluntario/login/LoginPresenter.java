package com.lutzed.servoluntario.login;

import android.text.TextUtils;

import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.api.requests.FacebookSignInRequest;
import com.lutzed.servoluntario.api.requests.SignInRequest;
import com.lutzed.servoluntario.models.User;
import com.lutzed.servoluntario.util.AuthHelper;
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
    private final AuthHelper mAuthHelper;

    public LoginPresenter(LoginFragment loginFragment, Api.ApiClient apiClient, AuthHelper authHelper) {
        mView = loginFragment;
        mApiClient = apiClient;
        mAuthHelper = authHelper;
        mView.setPresenter(this);
    }

    @Override
    public void attemptEmailLogin(String email, String password) {
        mView.resetErrors();

        boolean cancel = false;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mView.showEmailRequiredError();
            mView.setFocusEmailField();
            cancel = true;
        } else if (!Snippets.isEmailValid(email)) {
            mView.showInvalidEmailError();
            mView.setFocusEmailField();
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
            mView.setSavingProgress(true);
            mApiClient.signIn(new SignInRequest(email, password)).enqueue(
                    new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            mView.setSavingProgress(false);
                            switch (response.code()) {
                                case 200:
                                    mAuthHelper.setUser(response.body());
                                    mView.navigateToMain();
                                    break;
                                case 401:
                                    mView.showInvalidPasswordError();
                                    mView.setFocusPasswordField();
                                    break;
                                default:
                                    mView.showLoginDefaultError();
                                    mView.setFocusPasswordField();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            mView.setSavingProgress(false);
                            mView.showLoginDefaultError();
                            mView.setFocusPasswordField();
                        }
                    }
            );
        }

    }

    @Override
    public void recoveryPassword() {
        mView.showPasswordRecovery();
    }

    @Override
    public void startFacebookLogin() {
        mView.showFacebookLogin();
    }

    @Override
    public void volunteerSignUp() {
        mView.showVolunteerSignUp(false);
    }

    @Override
    public void organizationSignUp() {
        mView.showOrganizationSignUp();
    }

    @Override
    public void attemptFacebookLogin(String token) {
        mView.setSavingProgress(true);
        mApiClient.signIn(new FacebookSignInRequest(token)).enqueue(
                new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        mView.setSavingProgress(false);
                        switch (response.code()) {
                            case 200:
                                mAuthHelper.setUser(response.body());
                                mView.navigateToMain();
                                break;
                            case 404:
                                mView.showVolunteerSignUp(true);
                                break;
                            case 401:
                                mView.showFacebookLoginError();
                                break;
                            default:
                                mView.showFacebookLoginError();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        mView.setSavingProgress(false);
                        mView.showFacebookLoginError();
                        mView.setFocusPasswordField();
                    }
                }
        );
    }

    @Override
    public void start() {
        if (mAuthHelper.hasUser()) {
            mView.setSavingProgress(true);
            mView.navigateToMain();
            mAuthHelper.updateUserData();
        }
    }
}
