package com.lutzed.servoluntario.completion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.util.ActivityUtils;
import com.lutzed.servoluntario.util.AuthHelper;

public class CompletionActivity extends AppCompatActivity {

    private VolunteerCompletionPresenter mVolunteerCompletionPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completion);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        switch (AuthHelper.getInstance(this).getUser().getKindEnum()) {
            case VOLUNTEER:
                setupVolunteerCompletion(savedInstanceState);
                break;
            case ORGANIZATION:
                setupOrganizationCompletion(savedInstanceState);
                break;
        }

    }

    private void setupOrganizationCompletion(Bundle savedInstanceState) {
        VolunteerCompletionFragment volunteerCompletionFragment = (VolunteerCompletionFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (volunteerCompletionFragment == null) {
            // Create the fragment
            volunteerCompletionFragment = VolunteerCompletionFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), volunteerCompletionFragment, R.id.contentFrame);
        }

        AuthHelper authHelper = AuthHelper.getInstance(this);
        // Create the presenter
        mVolunteerCompletionPresenter = new VolunteerCompletionPresenter(volunteerCompletionFragment, Api.getClient(authHelper.getUser()), authHelper);

        // Load previously saved state, if available.
        if (savedInstanceState != null) {

        }
    }

    private void setupVolunteerCompletion(Bundle savedInstanceState) {
        VolunteerCompletionFragment volunteerCompletionFragment = (VolunteerCompletionFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (volunteerCompletionFragment == null) {
            // Create the fragment
            volunteerCompletionFragment = VolunteerCompletionFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), volunteerCompletionFragment, R.id.contentFrame);
        }

        AuthHelper authHelper = AuthHelper.getInstance(this);
        // Create the presenter
        mVolunteerCompletionPresenter = new VolunteerCompletionPresenter(volunteerCompletionFragment, Api.getClient(authHelper.getUser()), authHelper);

        // Load previously saved state, if available.
        if (savedInstanceState != null) {

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}