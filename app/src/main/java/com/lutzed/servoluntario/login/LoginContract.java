package com.lutzed.servoluntario.login;

import com.lutzed.servoluntario.BasePresenter;
import com.lutzed.servoluntario.BaseView;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public interface LoginContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);
        void navigateToMain();
        void resetLoginErrors();

        void showSignUp();

        void showEmailRequiredError();
        void showPasswordRequiredError();
        void showInvalidEmailError();
        void showInvalidPasswordError();
        void setFocusPasswordField();
        void setFocusEmailField();
        void showLoginDefaultError();

        void showFacebookLoginError();
    }

    interface Presenter extends BasePresenter {
        void attemptEmailLogin(String email, String password);

        void attemptFacebookLogin(String token);
    }
}
