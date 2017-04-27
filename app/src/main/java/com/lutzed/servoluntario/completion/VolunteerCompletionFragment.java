package com.lutzed.servoluntario.completion;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lutzed.servoluntario.R.id.about;

/**
 * A login screen that offers login via email/password.
 */
public class VolunteerCompletionFragment extends Fragment implements VolunteerCompletionContract.View {

    @BindView(about) EditText mAboutView;
    @BindView(R.id.occupation) EditText mOccupationView;
    @BindView(R.id.progress) View mProgressView;
    @BindView(R.id.volunteer_completion_form) View mLoginFormView;

    private VolunteerCompletionContract.Presenter mPresenter;

    public static VolunteerCompletionFragment newInstance() {
        return new VolunteerCompletionFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(VolunteerCompletionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_completion, container, false);
        ButterKnife.bind(this, root);

        mOccupationView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.save || id == EditorInfo.IME_NULL) {
                    onSaveClicked();
                    return true;
                }
                return false;
            }
        });

        return root;
    }

    @OnClick(R.id.save_button)
    void onSaveClicked() {
        mPresenter.saveProfile(mAboutView.getText().toString(), mOccupationView.getText().toString());
    }

    public void resetErrors() {
       mAboutView.setError(null);
       mOccupationView.setError(null);
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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void setAboutField(String about) {
        mAboutView.setText(about);
    }

    @Override
    public void setOccupationField(String occupation) {
        mOccupationView.setText(occupation);
    }

    @Override
    public void showDefaultSaveError() {
        Toast.makeText(getContext(), "DEFAULT SAVE ERROR", Toast.LENGTH_SHORT).show();
    }
}

