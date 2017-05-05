package com.lutzed.servoluntario.signup;

import android.os.Bundle;
import android.text.TextUtils;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.Organization;
import com.lutzed.servoluntario.models.User;
import com.lutzed.servoluntario.models.Volunteer;
import com.lutzed.servoluntario.util.AuthHelper;
import com.lutzed.servoluntario.util.Snippets;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private boolean mIsFacebookSignUp;
    private String mGender;

    public SignUpPresenter(SignUpFragment signUpFragment, Api.ApiClient apiClient, AuthHelper authHelper, User.Kind kind, boolean isFacebookSignUp) {
        mView = signUpFragment;
        mApiClient = apiClient;
        mAuthHelper = authHelper;
        mCurrentSignUpUserKind = kind;
        mIsFacebookSignUp = isFacebookSignUp;
        mView.setPresenter(this);
    }

    @Override
    public void attemptSignUp(String name, String username, String email, String password, String phoneNumber) {
        mView.resetErrors();

        boolean cancel = false;

        // Check for a valid name
        if (TextUtils.isEmpty(name)) {
            mView.showNameRequiredError();
            mView.setFocusNameField();
            cancel = true;
        }

        // Check for a valid phoneNumber ONLY if Organization.
        if (mCurrentSignUpUserKind == ORGANIZATION) {
            if (TextUtils.isEmpty(phoneNumber)) {
                mView.showPhoneRequiredError();
                if (!cancel) mView.setFocusPhoneField();
                cancel = true;
            } else if (!Snippets.isPhoneValid(phoneNumber)) {
                mView.showInvalidPhoneError();
                if (!cancel) mView.setFocusPhoneField();
                cancel = true;
            }
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(username)) {
            mView.showUsernameRequiredError();
            if (!cancel) mView.setFocusUsernameField();
            cancel = true;
        } else if (!Snippets.isUsernameValid(username)) {
            mView.showInvalidUsernameError();
            if (!cancel) mView.setFocusUsernameField();
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mView.showEmailRequiredError();
            if (!cancel) mView.setFocusEmailField();
            cancel = true;
        } else if (!Snippets.isEmailValid(email)) {
            mView.showInvalidEmailError();
            if (!cancel) mView.setFocusEmailField();
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

            User user = new User();
            user.setKind(mCurrentSignUpUserKind.toString());
            switch (mCurrentSignUpUserKind) {
                case VOLUNTEER:
                    Volunteer volunteer = new Volunteer();

                    volunteer.setName(name);
                    volunteer.setGender(mGender);

                    user.setVolunteerAttributes(volunteer);

                    if (mIsFacebookSignUp)
                        user.setFacebookToken(AccessToken.getCurrentAccessToken().getToken());

                    break;
                case ORGANIZATION:
                    Organization organization = new Organization();

                    organization.setName(name);

                    List<Contact> contacts = new ArrayList<>();
                    Contact contact = new Contact();
                    contact.setPhone(phoneNumber);
                    contacts.add(contact);
                    organization.setContactsAttributes(contacts);

                    user.setOrganizationAttributes(organization);

                    break;
            }
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);

            mApiClient.createUser(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    mView.setLoadingIndicator(false);
                    switch (response.code()) {
                        case 201:
                            mAuthHelper.setUser(response.body());
                            mView.navigateToCompletion();
                            break;
                        case 422:
                            mView.showSignUpDefaultError();
                            break;
                        default:
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    mView.setLoadingIndicator(false);
                    mView.showSignUpDefaultError();
                    t.printStackTrace();
                }
            });
        }
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
    public void setIsFacebookSignUp(boolean isFacebookSignUp) {
        mIsFacebookSignUp = isFacebookSignUp;
    }

    @Override
    public boolean getIsFacebookSignUp() {
        return mIsFacebookSignUp;
    }

    @Override
    public void setSignUpUserKind(User.Kind kind) {
        mCurrentSignUpUserKind = kind;
        sendSignUpModeToView(mCurrentSignUpUserKind);
    }

    private void sendSignUpModeToView(User.Kind kind) {
        switch (kind) {
            case VOLUNTEER:
                mView.setupVolunteerSignUpPrompts();
                mView.setPhoneFieldVisibility(false);
                break;
            case ORGANIZATION:
                mView.setupOrganizationSignUpPrompts();
                mView.setPhoneFieldVisibility(true);
                break;
        }
        mView.clearAllFields();
        mView.setFocusNameField();
    }

    @Override
    public User.Kind getSignUpUserKind() {
        return mCurrentSignUpUserKind;
    }

    @Override
    public void start() {
        sendSignUpModeToView(mCurrentSignUpUserKind);
        if (mIsFacebookSignUp) populateFacebookData();
    }

    private void populateFacebookData() {
        mView.setLoadingIndicator(true);
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject me,
                            GraphResponse response) {
                        mView.setLoadingIndicator(false);
                        mView.populateFacebookFields(me.optString("name"), me.optString("email"));
                        mGender = me.optString("gender");
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
