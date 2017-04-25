package com.lutzed.servoluntario.signup;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.User;
import com.lutzed.servoluntario.util.ActivityUtils;
import com.lutzed.servoluntario.util.AuthHelper;

public class SignUpActivity extends AppCompatActivity {

    public static final String EXTRA_SIGN_UP_USER_KIND = "extraSignUpUserKind";
    public static final String EXTRA_IS_FACEBOOK_SIGN_UP = "extraIsFacebookSignUp";

    private SignUpPresenter mSignUpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        User.Kind singUpKind = (User.Kind) getIntent().getSerializableExtra(EXTRA_SIGN_UP_USER_KIND);
        boolean isFacebookSignUp = getIntent().getBooleanExtra(EXTRA_IS_FACEBOOK_SIGN_UP, false);

        SignUpFragment signUpFragment =
                (SignUpFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);


        if (signUpFragment == null) {
            // Create the fragment
            signUpFragment = SignUpFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), signUpFragment, R.id.contentFrame);
        }

        // Create the presenter
        mSignUpPresenter = new SignUpPresenter(signUpFragment, Api.getUnauthorizedClient(), AuthHelper.getInstance(this), singUpKind, isFacebookSignUp);

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
            singUpKind = (User.Kind) savedInstanceState.getSerializable(EXTRA_SIGN_UP_USER_KIND);
            mSignUpPresenter.setSignUpUserKind(singUpKind);

            isFacebookSignUp = savedInstanceState.getBoolean(EXTRA_SIGN_UP_USER_KIND, false);
            mSignUpPresenter.setIsFacebookSignUp(isFacebookSignUp);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_SIGN_UP_USER_KIND, mSignUpPresenter.getSignUpUserKind());
        outState.putSerializable(EXTRA_IS_FACEBOOK_SIGN_UP, mSignUpPresenter.getIsFacebookSignUp());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}