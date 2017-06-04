package com.lutzed.servoluntario.login;

import com.lutzed.servoluntario.interfaces.BasePresenter;
import com.lutzed.servoluntario.interfaces.BaseView;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public interface LoginContract {

    interface View extends BaseView<Presenter> {
        void setSavingProgress(boolean active);

        void navigateToMain();

        void resetErrors();

        void showVolunteerSignUp(boolean isFacebookSignUp);

        void showOrganizationSignUp();

        void showEmailRequiredError();

        void showPasswordRequiredError();

        void showInvalidEmailError();

        void showInvalidPasswordError();

        void setFocusPasswordField();

        void setFocusEmailField();

        void showLoginDefaultError();

        void showFacebookLoginError();

        void showFacebookLogin();

        void showPasswordRecovery();
    }

    interface Presenter extends BasePresenter {
        void attemptEmailLogin(String email, String password);

        void attemptFacebookLogin(String token);

        void startFacebookLogin();

        void recoveryPassword();

        void volunteerSignUp();

        void organizationSignUp();
    }
}
