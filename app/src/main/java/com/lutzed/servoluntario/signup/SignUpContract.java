package com.lutzed.servoluntario.signup;

import com.lutzed.servoluntario.BasePresenter;
import com.lutzed.servoluntario.BaseView;
import com.lutzed.servoluntario.models.User;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public interface SignUpContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void navigateToMain();

        void resetErrors();

        void showOrganizationSignUp();

        void showEmailRequiredError();

        void showNameRequiredError();

        void showUsernameRequiredError();

        void showInvalidUsernameError();

        void showPasswordRequiredError();

        void showInvalidEmailError();

        void showInvalidPasswordError();

        void setFocusPasswordField();

        void setFocusEmailField();

        void showSignUpDefaultError();

        void showVolunteerSignUp();

        void setFocusNameField();

        void setFocusUsernameField();

        void clearAllFields();

        void populateFacebookFields(String name, String email);
    }

    interface Presenter extends BasePresenter {
        void attemptSignUp(String name, String username, String email, String password);

        void setSignUpUserKind(User.Kind kind);

        User.Kind getSignUpUserKind();

        void toogleSignUpMode();

        void setIsFacebookSignUp(boolean isFacebookSignUp);

        boolean getIsFacebookSignUp();
    }
}
