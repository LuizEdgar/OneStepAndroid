package com.lutzed.servoluntario.volunteer;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Volunteer;
import com.lutzed.servoluntario.util.ActivityUtils;
import com.lutzed.servoluntario.util.AuthHelper;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VolunteerActivity extends AppCompatActivity implements VolunteerFragment.Listener {

    public static final String EXTRA_VOLUNTEER = "extra_volunteer";
    public static final String EXTRA_VOLUNTEER_ID = "extra_volunteer_id";

    @BindView(R.id.image) ImageView mImageView;

    private VolunteerPresenter mVolunteerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        VolunteerFragment volunteerFragment =
                (VolunteerFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);


        if (volunteerFragment == null) {
            // Create the fragment
            volunteerFragment = VolunteerFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), volunteerFragment, R.id.contentFrame);
        }

        AuthHelper authHelper = AuthHelper.getInstance(this);
        // Create the presenter

        if (getIntent().hasExtra(EXTRA_VOLUNTEER)) {
            Volunteer volunteer = getIntent().getParcelableExtra(EXTRA_VOLUNTEER);
            mVolunteerPresenter = new VolunteerPresenter(volunteerFragment, Api.getClient(authHelper.getUser()), authHelper, volunteer);
        } else if (getIntent().hasExtra(EXTRA_VOLUNTEER_ID)) {
            long id = getIntent().getLongExtra(EXTRA_VOLUNTEER_ID, 0);
            mVolunteerPresenter = new VolunteerPresenter(volunteerFragment, Api.getClient(authHelper.getUser()), authHelper, id);
        }

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