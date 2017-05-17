package com.lutzed.servoluntario.opportunity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.util.ActivityUtils;
import com.lutzed.servoluntario.util.AuthHelper;

public class CreateOpportunityActivity extends AppCompatActivity {

    private CreateOpportunityPresenter mCreateOpportunityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_opportunity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        CreateOpportunityFragment createOpportunityFragment =
                (CreateOpportunityFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);


        if (createOpportunityFragment == null) {
            // Create the fragment
            createOpportunityFragment = CreateOpportunityFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), createOpportunityFragment, R.id.contentFrame);
        }

        AuthHelper authHelper = AuthHelper.getInstance(this);
        // Create the presenter
        mCreateOpportunityPresenter = new CreateOpportunityPresenter(createOpportunityFragment, Api.getClient(authHelper.getUser()), authHelper);

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
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}