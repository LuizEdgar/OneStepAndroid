package com.lutzed.servoluntario.signup;

import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.User;
import com.lutzed.servoluntario.util.AuthHelper;

import static com.lutzed.servoluntario.models.User.Kind.ORGANIZATION;
import static com.lutzed.servoluntario.models.User.Kind.VOLUNTEER;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public class SignUpPresenter implements SignUpContract.Presenter {

    private final SignUpContract.View mView;
    private final Api.ApiClient mApiClient;
    private final AuthHelper mAuthHelper;
    private User.Kind mCurrentSignUpUserKind;

    public SignUpPresenter(SignUpFragment signUpFragment, Api.ApiClient apiClient, AuthHelper authHelper, User.Kind kind) {
        mView = signUpFragment;
        mApiClient = apiClient;
        mAuthHelper = authHelper;
        mCurrentSignUpUserKind = kind;
        mView.setPresenter(this);
    }

    @Override
    public void attemptSignUp(String name, String email, String password) {

    }

    @Override
    public void toogleSignUpMode() {
        switch (mCurrentSignUpUserKind) {
            case VOLUNTEER:
                setSignUpUserKind(ORGANIZATION);
                break;
            case ORGANIZATION:
                setSignUpUserKind(VOLUNTEER);
                break;
        }
    }

    @Override
    public void setSignUpUserKind(User.Kind kind) {
        mCurrentSignUpUserKind = kind;
        sendSignUpModeToView(mCurrentSignUpUserKind);
    }

    private void sendSignUpModeToView(User.Kind kind){
        switch (kind) {
            case VOLUNTEER:
                mView.showVolunteerSignUp();
                break;
            case ORGANIZATION:
                mView.showOrganizationSignUp();
                break;
        }
    }

    @Override
    public User.Kind getSignUpUserKind() {
        return mCurrentSignUpUserKind;
    }

    @Override
    public void start() {
        sendSignUpModeToView(mCurrentSignUpUserKind);
    }

}
