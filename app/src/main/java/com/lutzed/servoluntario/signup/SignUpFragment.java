package com.lutzed.servoluntario.signup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.IntentCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.activities.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lutzed.servoluntario.R.id.email;

/**
 * A login screen that offers login via email/password.
 */
public class SignUpFragment extends Fragment implements SignUpContract.View {

    @BindView(email) AutoCompleteTextView mEmailView;
    @BindView(R.id.name) EditText mNameView;
    @BindView(R.id.username) EditText mUsernameView;
    @BindView(R.id.password) EditText mPasswordView;
    @BindView(R.id.login_progress) View mProgressView;
    @BindView(R.id.email_login_form) View mLoginFormView;
    @BindView(R.id.toggle_sign_up_mode_button) TextView mToggleSignUpModeButton;

    private SignUpContract.Presenter mPresenter;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(SignUpContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, root);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.sign_up || id == EditorInfo.IME_NULL) {
                    onSignUpClicked();
                    return true;
                }
                return false;
            }
        });

        return root;
    }

    @OnClick(R.id.sign_up_button)
    void onSignUpClicked() {
        mPresenter.attemptSignUp(mNameView.getText().toString(), mUsernameView.getText().toString(), mEmailView.getText().toString(), mPasswordView.getText().toString());
    }

    public void resetErrors() {
        mNameView.setError(null);
        mEmailView.setError(null);
        mUsernameView.setError(null);
        mPasswordView.setError(null);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void setLoadingIndicator(final boolean active) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(active ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    active ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(active ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(active ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(active ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(active ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(active ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void clearAllFields() {
        mNameView.setText("");
        mEmailView.setText("");
        mUsernameView.setText("");
        mPasswordView.setText("");
    }

    @Override
    public void navigateToMain() {
        getActivity().finish();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.toggle_sign_up_mode_button)
    public void onOrganizationSignUpClicked() {
        mPresenter.toogleSignUpMode();
    }

    @Override
    public void showOrganizationSignUp() {
        mToggleSignUpModeButton.setText(R.string.action_toggle_to_volunteer);
    }

    @Override
    public void showVolunteerSignUp() {
        mToggleSignUpModeButton.setText(R.string.action_toggle_to_organization);
    }

    @Override
    public void setFocusNameField() {
        mNameView.requestFocus();
    }

    @Override
    public void setFocusUsernameField() {
        mUsernameView.requestFocus();
    }

    @Override
    public void showEmailRequiredError() {
        mEmailView.setError(getString(R.string.error_field_required));
    }

    @Override
    public void showNameRequiredError() {
        mNameView.setError(getString(R.string.error_field_required));
    }

    @Override
    public void showUsernameRequiredError() {
        mUsernameView.setError(getString(R.string.error_field_required));
    }

    @Override
    public void showInvalidUsernameError() {
        mUsernameView.setError(getString(R.string.error_invalid_username));
    }

    @Override
    public void showPasswordRequiredError() {
        mPasswordView.setError(getString(R.string.error_field_required));
    }

    @Override
    public void showInvalidEmailError() {
        mEmailView.setError(getString(R.string.error_invalid_email));
    }

    @Override
    public void showInvalidPasswordError() {
        mPasswordView.setError(getString(R.string.error_invalid_password));
    }

    @Override
    public void setFocusPasswordField() {
        mPasswordView.requestFocus();
    }

    @Override
    public void setFocusEmailField() {
        mEmailView.requestFocus();
    }

    @Override
    public void showSignUpDefaultError() {
        Toast.makeText(getContext(), "TODO Defaul signUp error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void populateFacebookFields(String name, String email) {
        mNameView.setText(name);
        mEmailView.setText(email);
    }
}

