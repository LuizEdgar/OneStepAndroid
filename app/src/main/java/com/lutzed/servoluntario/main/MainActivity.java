package com.lutzed.servoluntario.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.opportunities.OpportunitiesActivity;
import com.lutzed.servoluntario.util.AuthHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String HOME_TAG = "home_tag";
    private static final String DO_TAG = "do_tag";
    private static final String PROFILE_TAG = "profile_tag";

    private Fragment mCurrentFragment;

    private Api.ApiClient mApiClient;
    private FeedPresenter mHomePresenter;

    private FeedFragment mHomeFragment;
    private PlaceHolderFragment mDoFragment;
    private PlaceHolderFragment mProfileFragment;

    @BindView(R.id.mainFrame) FrameLayout mSectionContainerLayout;
    @BindView(R.id.navigation) BottomNavigationView mNavigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    navigateToHome();
                    break;
                case R.id.navigation_do:
                    navigateToDo();

                    AuthHelper.getInstance(MainActivity.this).signout();
                    finish();
                    break;
                case R.id.navigation_profile:
                    navigateToProfile();

                    Api.getClient(AuthHelper.getInstance(MainActivity.this).getUser()).getOpportunity(11l).enqueue(new Callback<Opportunity>() {
                        @Override
                        public void onResponse(Call<Opportunity> call, Response<Opportunity> response) {
                            Intent intent = new Intent(MainActivity.this, OpportunitiesActivity.class);
                            intent.putExtra(OpportunitiesActivity.EXTRA_OPPORTUNITY, response.body());
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Opportunity> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mApiClient = Api.getClient(AuthHelper.getInstance(this).getUser());

        initializeAndRestoreFragments(savedInstanceState);
//        mNavigation.setSelectedItemId();

        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void navigateToProfile() {
        if (mProfileFragment == null) {
            mProfileFragment = new PlaceHolderFragment();
            addFragment(mProfileFragment, PROFILE_TAG);

        } else {
            showFragment(mProfileFragment);
        }
    }

    private void navigateToDo() {
        if (mDoFragment == null) {
            mDoFragment = new PlaceHolderFragment();
            addFragment(mDoFragment, DO_TAG);
        } else {
            showFragment(mDoFragment);
        }
    }

    private void navigateToHome() {
        if (mHomeFragment == null) {
            mHomeFragment = FeedFragment.newInstance();
            addFragment(mHomeFragment, HOME_TAG);
            mHomePresenter = new FeedPresenter(mHomeFragment, mApiClient);
            mHomeFragment.start();
        } else {
            showFragment(mHomeFragment);
        }
    }

    private void initializeAndRestoreFragments(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // attempt to restore the fragments
        if (savedInstanceState != null) {

            // restore all section fragments
            mHomeFragment = (FeedFragment) fragmentManager.findFragmentByTag(HOME_TAG);
            if (mHomeFragment != null && mHomePresenter == null){
                mHomePresenter = new FeedPresenter(mHomeFragment, mApiClient);
            }
            mDoFragment = (PlaceHolderFragment) fragmentManager.findFragmentByTag(DO_TAG);
            mProfileFragment = (PlaceHolderFragment) fragmentManager.findFragmentByTag(PROFILE_TAG);

            // restore current section Fragment
            if (isCurrentFragment(mHomeFragment)) {
                mCurrentFragment = mHomeFragment;
                mHomeFragment.start();
            } else if (isCurrentFragment(mDoFragment)) {
                mCurrentFragment = mDoFragment;

            } else if (isCurrentFragment(mProfileFragment)) {
                mCurrentFragment = mProfileFragment;

            } else {
                throw new IllegalStateException("Unable to restore current section Fragment");
            }
        }

        // if no Fragments were restored, then display the home section
        if (fragmentManager.findFragmentById(R.id.mainFrame) == null) {
            navigateToHome();
        }
    }

    private boolean isCurrentFragment(@Nullable Fragment sectionContainerFragment) {
        return sectionContainerFragment != null && !sectionContainerFragment.isHidden();
    }

    private void addFragment(@NonNull Fragment sectionContainerFragment, @NonNull String tag) {
        mCurrentFragment = sectionContainerFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        hideSectionIfNotCurrent(mHomeFragment, mCurrentFragment, transaction);
        hideSectionIfNotCurrent(mDoFragment, mCurrentFragment, transaction);
        hideSectionIfNotCurrent(mProfileFragment, mCurrentFragment, transaction);

        transaction.add(R.id.mainFrame, sectionContainerFragment, tag);
        transaction.commitNow();
    }

    private void showFragment(@NonNull Fragment sectionContainerFragment) {
        mCurrentFragment = sectionContainerFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        hideSectionIfNotCurrent(mHomeFragment, mCurrentFragment, transaction);
        hideSectionIfNotCurrent(mDoFragment, mCurrentFragment, transaction);
        hideSectionIfNotCurrent(mProfileFragment, mCurrentFragment, transaction);

        // show the fragment we're interested in
        transaction.show(sectionContainerFragment);
        transaction.commitNow();
    }

    private void hideSectionIfNotCurrent(
            @Nullable Fragment fragment,
            @NonNull Fragment currentFragment,
            @NonNull FragmentTransaction transaction) {
        if (fragment != null && fragment != currentFragment) transaction.hide(fragment);
    }

}
