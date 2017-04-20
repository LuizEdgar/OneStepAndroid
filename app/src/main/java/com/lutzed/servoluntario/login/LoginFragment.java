package com.lutzed.servoluntario.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.activities.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lutzed.servoluntario.R.id.email;

/**
 * A login screen that offers login via email/password.
 */
public class LoginFragment extends Fragment implements LoginContract.View {

    @BindView(email) AutoCompleteTextView mEmailView;
    @BindView(R.id.password) EditText mPasswordView;
    @BindView(R.id.login_progress) View mProgressView;
    @BindView(R.id.email_login_form) View mLoginFormView;
    @BindView(R.id.login_button) LoginButton mLoginButton;

    private LoginContract.Presenter mPresenter;

    private CallbackManager mCallbackManager;
    private FacebookCallback mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            mPresenter.attemptFacebookLogin(loginResult.getAccessToken().getToken());
        }

        @Override
        public void onCancel() {
            Log.d("FBL", "cancel");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("FBL", error.getMessage());
        }
    };


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, root);

        Button mEmailSignInButton = (Button) root.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignInClicked();
            }
        });

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    onSignInClicked();
                    return true;
                }
                return false;
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.setReadPermissions("email");
        mLoginButton.setFragment(this);
        mLoginButton.registerCallback(mCallbackManager, mFacebookCallback);

        return root;
    }

    @OnClick(R.id.email_sign_in_button)
    void onSignInClicked() {
        mPresenter.attemptEmailLogin(mEmailView.getText().toString(), mPasswordView.getText().toString());
    }

    @Override
    public void resetLoginErrors() {
        mEmailView.setError(null);
        mPasswordView.setError(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void showSignUp() {
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
    public void showInvalidPasswordError() {
        mPasswordView.setError(getString(R.string.error_invalid_password));
    }

    @Override
    public void showLoginDefaultError() {
        mPasswordView.setError(getString(R.string.error_try_later));
    }

    @Override
    public void showFacebookLoginError() {
        Toast.makeText(getContext(), getString(R.string.error_facebook_login), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setFocusPasswordField() {
        mPasswordView.requestFocus();
    }

    @Override
    public void setFocusEmailField() {
        mEmailView.requestFocus();
    }
}

