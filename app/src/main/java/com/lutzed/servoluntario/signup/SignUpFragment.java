package com.lutzed.servoluntario.signup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.IntentCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.completion.CompletionActivity;
import com.lutzed.servoluntario.util.Snippets;
import com.lutzed.servoluntario.util.TextInputLayoutTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class SignUpFragment extends Fragment implements SignUpContract.View {

    @BindView(R.id.email) AutoCompleteTextView mEmailView;
    @BindView(R.id.name) EditText mNameView;
    @BindView(R.id.username) EditText mUsernameView;
    @BindView(R.id.password) EditText mPasswordView;
    @BindView(R.id.phone) EditText mPhoneView;
    @BindView(R.id.progress) View mProgressView;
    @BindView(R.id.sign_up_form) View mLoginFormView;
    @BindView(R.id.toggle_sign_up_mode_button) TextView mToggleSignUpModeButton;

    @BindView(R.id.emailInputLayout) TextInputLayout mEmailInputLayout;
    @BindView(R.id.nameInputLayout) TextInputLayout mNameInputLayout;
    @BindView(R.id.usernameInputLayout) TextInputLayout mUsernameInputLayout;
    @BindView(R.id.passwordInputLayout) TextInputLayout mPasswordInputLayout;
    @BindView(R.id.phoneInputLayout) TextInputLayout mPhoneInputLayout;

    private SignUpContract.Presenter mPresenter;
    private ProgressDialog mLoadingProgress;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
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

        mEmailView.addTextChangedListener(new TextInputLayoutTextWatcher(mEmailView, mEmailInputLayout));
        mNameView.addTextChangedListener(new TextInputLayoutTextWatcher(mNameView, mNameInputLayout));
        mUsernameView.addTextChangedListener(new TextInputLayoutTextWatcher(mUsernameView, mUsernameInputLayout));
        mPasswordView.addTextChangedListener(new TextInputLayoutTextWatcher(mPasswordView, mPasswordInputLayout));
        mPhoneView.addTextChangedListener(new TextInputLayoutTextWatcher(mPhoneView, mPhoneInputLayout));

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sign_up, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sign_up) {
            onSignUpClicked();
        }
        return super.onOptionsItemSelected(item);
    }

    void onSignUpClicked() {
        mPresenter.attemptSignUp(mNameView.getText().toString().trim(), mUsernameView.getText().toString().trim().toLowerCase(), mEmailView.getText().toString().trim(), mPasswordView.getText().toString(), mPhoneView.getText().toString());
    }

    public void resetErrors() {
        mNameInputLayout.setError(null);
        mEmailInputLayout.setError(null);
        mUsernameInputLayout.setError(null);
        mPasswordInputLayout.setError(null);
        mPhoneInputLayout.setError(null);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void setSavingIndicator(final boolean active) {
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
    public void setLoadingIndicator(boolean active) {
        if (active) {
            if (mLoadingProgress == null) {
                mLoadingProgress = Snippets.createProgressDialog(getContext(), R.string.loading_progress);
                mLoadingProgress.setCancelable(false);
            }
            if (!mLoadingProgress.isShowing()) {
                mLoadingProgress.show();
            }
        } else {
            if (mLoadingProgress != null && mLoadingProgress.isShowing()) {
                mLoadingProgress.dismiss();
                mLoadingProgress = null;
            }
        }

    }

    @Override
    public void clearAllFields() {
        mNameView.setText("");
        mEmailView.setText("");
        mUsernameView.setText("");
        mPasswordView.setText("");
        mPhoneView.setText("");
    }

    @Override
    public void navigateToCompletion() {
        getActivity().finish();
        Intent intent = new Intent(getContext(), CompletionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.toggle_sign_up_mode_button)
    public void onOrganizationSignUpClicked() {
        mPresenter.toogleSignUpMode();
    }

    @Override
    public void setupOrganizationSignUpPrompts() {
        mNameInputLayout.setHint(getString(R.string.prompt_organization_name));
        mToggleSignUpModeButton.setText(R.string.action_toggle_to_volunteer);
    }

    @Override
    public void setupVolunteerSignUpPrompts() {
        mNameInputLayout.setHint(getString(R.string.name));
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
        mEmailInputLayout.setError(getString(R.string.error_field_required));
    }

    @Override
    public void showNameRequiredError() {
        mNameInputLayout.setError(getString(R.string.error_field_required));
    }

    @Override
    public void showUsernameRequiredError() {
        mUsernameInputLayout.setError(getString(R.string.error_field_required));
    }

    @Override
    public void showInvalidUsernameError() {
        mUsernameInputLayout.setError(getString(R.string.error_invalid_username));
    }

    @Override
    public void showPasswordRequiredError() {
        mPasswordInputLayout.setError(getString(R.string.error_field_required));
    }

    @Override
    public void showInvalidEmailError() {
        mEmailInputLayout.setError(getString(R.string.error_invalid_email));
    }

    @Override
    public void showInvalidPasswordError() {
        mPasswordInputLayout.setError(getString(R.string.error_invalid_password));
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

    @Override
    public void setPhoneFieldVisibility(boolean isVisible) {
        mPhoneView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showPhoneRequiredError() {
        mPhoneInputLayout.setError(getString(R.string.error_field_required));
    }

    @Override
    public void setFocusPhoneField() {
        mPhoneView.requestFocus();
    }

    @Override
    public void showInvalidPhoneError() {
        mPhoneInputLayout.setError(getString(R.string.error_invalid_phone));
    }
}

