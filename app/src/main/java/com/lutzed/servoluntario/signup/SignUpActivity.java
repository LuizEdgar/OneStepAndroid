package com.lutzed.servoluntario.signup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.User;
import com.lutzed.servoluntario.util.ActivityUtils;
import com.lutzed.servoluntario.util.AuthHelper;

public class SignUpActivity extends AppCompatActivity{

    public static final String EXTRA_SIGN_UP_USER_KIND = "extraSignUpUserKind";

    private SignUpPresenter mSignUpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        User.Kind singUpKind = (User.Kind) getIntent().getSerializableExtra(EXTRA_SIGN_UP_USER_KIND);

        SignUpFragment signUpFragment =
                (SignUpFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);


        if (signUpFragment == null) {
            // Create the fragment
            signUpFragment = SignUpFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), signUpFragment, R.id.contentFrame);
        }

        // Create the presenter
        mSignUpPresenter = new SignUpPresenter(signUpFragment, Api.getUnauthorizedClient(), AuthHelper.getInstance(this), singUpKind);

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
            singUpKind = (User.Kind) savedInstanceState.getSerializable(EXTRA_SIGN_UP_USER_KIND);
            mSignUpPresenter.setSignUpUserKind(singUpKind);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_SIGN_UP_USER_KIND, mSignUpPresenter.getSignUpUserKind());
        super.onSaveInstanceState(outState);
    }


}