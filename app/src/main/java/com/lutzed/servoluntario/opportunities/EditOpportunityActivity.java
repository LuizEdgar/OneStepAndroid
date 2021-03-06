package com.lutzed.servoluntario.opportunities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.util.ActivityUtils;
import com.lutzed.servoluntario.util.AuthHelper;

public class EditOpportunityActivity extends AppCompatActivity {

    public static final String EXTRA_OPPORTUNITY = "extra_opportunity";

    private EditOpportunityPresenter mEditOpportunityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_opportunity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        EditOpportunityFragment editOpportunityFragment =
                (EditOpportunityFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);


        if (editOpportunityFragment == null) {
            // Create the fragment
            editOpportunityFragment = EditOpportunityFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), editOpportunityFragment, R.id.contentFrame);
        }

        AuthHelper authHelper = AuthHelper.getInstance(this);
        // Create the presenter

        Opportunity opportunity = getIntent().getParcelableExtra(EXTRA_OPPORTUNITY);
        mEditOpportunityPresenter = new EditOpportunityPresenter(editOpportunityFragment, Api.getClient(authHelper.getUser()), authHelper, opportunity);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mEditOpportunityPresenter != null){
            mEditOpportunityPresenter.onRequestPermissionResult(requestCode, permissions, grantResults);
        }
    }
}