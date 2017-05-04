package com.lutzed.servoluntario.selection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.util.ActivityUtils;
import com.lutzed.servoluntario.util.AuthHelper;

public class ItemsSelectionActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_SELECTION_KIND = "extra_item_selection_kind";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Kind kind = (Kind) getIntent().getSerializableExtra(EXTRA_ITEM_SELECTION_KIND);
        if (kind == Kind.SKILL) {
            setupSkillItemSelectionKind(savedInstanceState);
        } else if (kind == Kind.CAUSE) {
            setupCauseItemSelectionKind(savedInstanceState);
        }
    }

    private void setupSkillItemSelectionKind(Bundle savedInstanceState) {
        ItemsSelectionFragment fragment =
                (ItemsSelectionFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            // Create the fragment
            fragment = ItemsSelectionFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.contentFrame);
        }

        // Create the presenter
        SkillsSelectionPresenter presenter = new SkillsSelectionPresenter(fragment, Api.getClient(AuthHelper.getInstance(this).getUser()), AuthHelper.getInstance(this));

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
        }
    }

    public void setupCauseItemSelectionKind(Bundle savedInstanceState) {
        ItemsSelectionFragment fragment =
                (ItemsSelectionFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            // Create the fragment
            fragment = ItemsSelectionFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.contentFrame);
        }

        // Create the presenter
        CauseSelectionPresenter presenter = new CauseSelectionPresenter(fragment, Api.getClient(AuthHelper.getInstance(this).getUser()), AuthHelper.getInstance(this));

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
        }
    }

    public enum Kind {
        SKILL,
        CAUSE;
    }
}