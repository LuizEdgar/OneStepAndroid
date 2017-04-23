package com.lutzed.servoluntario.signup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
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
        mPresenter.attemptSignUp(null,null,null);
    }

    public void resetSignUpErrors() {
        mEmailView.setError(null);
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
    public void navigateToMain() {
        getActivity().finish();
        Intent intent = new Intent(getContext(), MainActivity.class);
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
    public void showEmailRequiredError() {
        mEmailView.setError(getString(R.string.error_field_required));
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
    public void showPasswordNotMatchError() {
        mPasswordView.setError(getString(R.string.error_password_match));
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

}

