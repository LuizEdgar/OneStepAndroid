package com.lutzed.servoluntario.organization;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Organization;
import com.lutzed.servoluntario.util.ActivityUtils;
import com.lutzed.servoluntario.util.AuthHelper;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrganizationActivity extends AppCompatActivity implements OrganizationFragment.Listener{

    public static final String EXTRA_ORGANIZATION = "extra_organization";

    @BindView(R.id.image) ImageView mImageView;

    private OrganizationPresenter mOrganizationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        OrganizationFragment organizationFragment =
                (OrganizationFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);


        if (organizationFragment == null) {
            // Create the fragment
            organizationFragment = OrganizationFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), organizationFragment, R.id.contentFrame);
        }

        AuthHelper authHelper = AuthHelper.getInstance(this);
        // Create the presenter

        Organization opportunity = getIntent().getParcelableExtra(EXTRA_ORGANIZATION);
        mOrganizationPresenter = new OrganizationPresenter(organizationFragment, Api.getClient(authHelper.getUser()), authHelper, opportunity);

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
    public void onCoverImage(String url) {
        Picasso.with(this).load(url).into(mImageView);
    }
}