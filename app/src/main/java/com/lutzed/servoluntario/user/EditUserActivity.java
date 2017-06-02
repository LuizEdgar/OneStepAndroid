package com.lutzed.servoluntario.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.util.ActivityUtils;
import com.lutzed.servoluntario.util.AuthHelper;

public class EditUserActivity extends AppCompatActivity {

    private EditUserPresenter mEditUserPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        EditUserFragment editUserFragment =
                (EditUserFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);


        if (editUserFragment == null) {
            // Create the fragment
            editUserFragment = EditUserFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), editUserFragment, R.id.contentFrame);
        }

        AuthHelper authHelper = AuthHelper.getInstance(this);
        mEditUserPresenter = new EditUserPresenter(editUserFragment, Api.getClient(authHelper.getUser()), authHelper);

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
        if (mEditUserPresenter != null){
            mEditUserPresenter.onRequestPermissionResult(requestCode, permissions, grantResults);
        }
    }
}