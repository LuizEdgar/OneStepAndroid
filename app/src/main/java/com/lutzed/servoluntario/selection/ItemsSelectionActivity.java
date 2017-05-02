package com.lutzed.servoluntario.selection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.util.ActivityUtils;
import com.lutzed.servoluntario.util.AuthHelper;

public class ItemsSelectionActivity extends AppCompatActivity{

    private SkillsSelectionPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ItemsSelectionFragment fragment =
                (ItemsSelectionFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            // Create the fragment
            fragment = ItemsSelectionFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.contentFrame);
        }

        // Create the presenter
        mPresenter = new SkillsSelectionPresenter(fragment, Api.getClient(AuthHelper.getInstance(this).getUser()));

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
        }
    }
}