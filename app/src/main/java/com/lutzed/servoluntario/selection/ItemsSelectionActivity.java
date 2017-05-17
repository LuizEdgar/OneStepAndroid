package com.lutzed.servoluntario.selection;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.SelectableItem;
import com.lutzed.servoluntario.util.ActivityUtils;
import com.lutzed.servoluntario.util.AuthHelper;

public class ItemsSelectionActivity extends AppCompatActivity implements ItemsSelectionFragment.OnListFragmentInteractionListener {

    public static final String EXTRA_ITEM_SELECTION_KIND = "extra_item_selection_kind";
    public static final String EXTRA_ITEM_SELECTION_MODE = "extra_item_selection_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Mode mode = (Mode) getIntent().getSerializableExtra(EXTRA_ITEM_SELECTION_MODE);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && mode == Mode.SINGLE) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Kind kind = (Kind) getIntent().getSerializableExtra(EXTRA_ITEM_SELECTION_KIND);
        if (kind == Kind.SKILL) {
            setupSkillItemSelectionKind(savedInstanceState, mode);
        } else if (kind == Kind.CAUSE) {
            setupCauseItemSelectionKind(savedInstanceState, mode);
        }

    }

    private void setupSkillItemSelectionKind(Bundle savedInstanceState, Mode mode) {
        ItemsSelectionFragment fragment =
                (ItemsSelectionFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            // Create the fragment
            fragment = ItemsSelectionFragment.newInstance(mode);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
        // Create the presenter
        SkillsSelectionPresenter presenter = new SkillsSelectionPresenter(fragment, Api.getClient(AuthHelper.getInstance(this).getUser()), AuthHelper.getInstance(this), mode);

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
        }
    }

    public void setupCauseItemSelectionKind(Bundle savedInstanceState, Mode mode) {
        ItemsSelectionFragment fragment =
                (ItemsSelectionFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            // Create the fragment
            fragment = ItemsSelectionFragment.newInstance(mode);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
        // Create the presenter
        CauseSelectionPresenter presenter = new CauseSelectionPresenter(fragment, Api.getClient(AuthHelper.getInstance(this).getUser()), AuthHelper.getInstance(this), mode);

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
        }
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
    public void onListFragmentInteraction(SelectableItem item, int position) {

    }

    public enum Kind {
        SKILL,
        CAUSE;
    }

    public enum Mode {
        SINGLE,
        MULTIPLE;
    }
}