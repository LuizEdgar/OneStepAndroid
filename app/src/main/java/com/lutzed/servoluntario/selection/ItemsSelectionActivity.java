package com.lutzed.servoluntario.selection;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.SelectableItem;
import com.lutzed.servoluntario.util.ActivityUtils;
import com.lutzed.servoluntario.util.AuthHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemsSelectionActivity extends AppCompatActivity implements ItemsSelectionFragment.OnListFragmentInteractionListener {

    public static final String EXTRA_ITEM_SELECTION_KIND = "extra_item_selection_kind";
    public static final String EXTRA_ITEM_SELECTION_MODE = "extra_item_selection_mode";

    public static final String EXTRA_SAVE_ACTION_NAME = "extra_save_action_name";
    public static final String EXTRA_SHOW_BACK = "extra_show_back";

    public static final String EXTRA_ITEM_SELECTION_IDS_CHECK = "extra_item_selection_ids_check";
    public static final String EXTRA_ITEM_SELECTION_IDS_EXCLUDE = "extra_item_selection_ids_exclude";

    public static final int EXTRA_SELECTION_REQUEST_CODE = 860;
    public static final String EXTRA_ITEMS_SELECTED = "extra_items_selected";
    public static final String EXTRA_ITEMS_NOT_SELECTED = "extra_items_not_selected";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Mode mode = (Mode) getIntent().getSerializableExtra(EXTRA_ITEM_SELECTION_MODE);

        ArrayList<Long> itensToExclude = new ArrayList<>();
        if (getIntent().hasExtra(EXTRA_ITEM_SELECTION_IDS_EXCLUDE)) {
            for (long l : getIntent().getLongArrayExtra(EXTRA_ITEM_SELECTION_IDS_EXCLUDE)) {
                itensToExclude.add(l);
            }
        }

        ArrayList<Long> itensToCheck = new ArrayList<>();
        if (getIntent().hasExtra(EXTRA_ITEM_SELECTION_IDS_CHECK)) {
            for (long l : getIntent().getLongArrayExtra(EXTRA_ITEM_SELECTION_IDS_CHECK)) {
                itensToCheck.add(l);
            }
        }

        String saveActionName = getIntent().getStringExtra(EXTRA_SAVE_ACTION_NAME);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(getIntent().getBooleanExtra(EXTRA_SHOW_BACK, false));
        }

        Kind kind = (Kind) getIntent().getSerializableExtra(EXTRA_ITEM_SELECTION_KIND);
        if (kind == Kind.SKILL) {
            setupSkillItemSelectionKind(savedInstanceState, mode, itensToExclude, itensToCheck, saveActionName);
        } else if (kind == Kind.CAUSE) {
            setupCauseItemSelectionKind(savedInstanceState, mode, itensToExclude, itensToCheck, saveActionName);
        }

    }

    private void setupSkillItemSelectionKind(Bundle savedInstanceState, Mode mode, ArrayList<Long> itensToExclude, ArrayList<Long> itensToCheck, String saveActionName) {
        ItemsSelectionFragment fragment =
                (ItemsSelectionFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            // Create the fragment
            fragment = ItemsSelectionFragment.newInstance(mode, saveActionName);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
        // Create the presenter
        SkillsSelectionPresenter presenter = new SkillsSelectionPresenter(fragment, Api.getClient(AuthHelper.getInstance(this).getUser()), AuthHelper.getInstance(this), mode, itensToExclude, itensToCheck);

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
        }
    }

    public void setupCauseItemSelectionKind(Bundle savedInstanceState, Mode mode, ArrayList<Long> itensToExclude, ArrayList<Long> itensToCheck, String saveActionName) {
        ItemsSelectionFragment fragment =
                (ItemsSelectionFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            // Create the fragment
            fragment = ItemsSelectionFragment.newInstance(mode, saveActionName);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
        // Create the presenter
        CauseSelectionPresenter presenter = new CauseSelectionPresenter(fragment, Api.getClient(AuthHelper.getInstance(this).getUser()), AuthHelper.getInstance(this), mode, itensToExclude, itensToCheck);

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
    public void onItemSelected(SelectableItem item) {
        ArrayList<SelectableItem> array = new ArrayList<>();
        array.add(item);
        onItemsSelected(array, null);
    }

    @Override
    public void onItemsSelected(List<SelectableItem> selectedItems, List<SelectableItem> notSelectedItems) {
        Intent resultIntent = new Intent();
        resultIntent.putParcelableArrayListExtra(EXTRA_ITEMS_SELECTED, (ArrayList<? extends Parcelable>) selectedItems);
        resultIntent.putParcelableArrayListExtra(EXTRA_ITEMS_NOT_SELECTED, (ArrayList<? extends Parcelable>) notSelectedItems);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public enum Kind {
        SKILL,
        CAUSE;
    }

    public enum Mode {
        SINGLE_SELECTION,
        MULTIPLE_SELECTION,
        MULTIPLE_SAVE_TO_USER;
    }
}