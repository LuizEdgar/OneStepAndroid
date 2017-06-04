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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.User;
import com.lutzed.servoluntario.opportunities.EditOpportunityActivity;
import com.lutzed.servoluntario.organization.OrganizationFragment;
import com.lutzed.servoluntario.organization.OrganizationPresenter;
import com.lutzed.servoluntario.util.AuthHelper;
import com.lutzed.servoluntario.volunteer.VolunteerFragment;
import com.lutzed.servoluntario.volunteer.VolunteerPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String HOME_TAG = "home_tag";
    private static final String EXPLORE_TAG = "explore_tag";
    private static final String OPPORTUNITIES_TAG = "opportunities_tag";
    private static final String VOLUNTEER_TAG = "volunteer_tag";
    private static final String ORGANIZATION_TAG = "organization_tag";

    private Fragment mCurrentFragment;

    private Api.ApiClient mApiClient;

    private FeedFragment mHomeFragment;
    private FeedPresenter mHomePresenter;
    private FeedFragment mOpportunitiesFragment;
    private FeedPresenter mOpportunitiesPresenter;
    private VolunteerPresenter mVolunteerPresenter;
    private VolunteerFragment mVolunteerFragment;
    private OrganizationPresenter mOrganizationPresenter;
    private OrganizationFragment mOrganizationFragment;

    private PlaceHolderFragment mExploreFragment;
    @BindView(R.id.mainFrame) FrameLayout mSectionContainerLayout;
    @BindView(R.id.navigation) BottomNavigationView mNavigation;
    @BindView(R.id.logo) ImageView mLogoView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    navigateToHome();
                    break;
                case R.id.navigation_explore:
                    navigateToExplore();
                    break;
                case R.id.navigation_create:
                    Intent intent = new Intent(MainActivity.this, EditOpportunityActivity.class);
                    startActivity(intent);
                    return false;
                case R.id.navigation_opportunities:
                    navigateToOpportunities();
                    break;
                case R.id.navigation_profile:
                    if (AuthHelper.getInstance(MainActivity.this).getUser().getKindEnum() == User.Kind.VOLUNTEER) {
                        navigateToVolunteerProfile();
                    } else {
                        navigateToOrganizationProfile();
                    }
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AuthHelper.getInstance(MainActivity.this).getUser().getKindEnum() == User.Kind.VOLUNTEER) {
            setContentView(R.layout.activity_main_volunteer);
        } else {
            setContentView(R.layout.activity_main_organization);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mApiClient = Api.getClient(AuthHelper.getInstance(this).getUser());

        initializeAndRestoreFragments(savedInstanceState);

        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void navigateToHome() {
        if (mHomeFragment == null) {
            mHomeFragment = FeedFragment.newInstance();
            addFragment(mHomeFragment, HOME_TAG);
            mHomePresenter = new FeedPresenter(mHomeFragment, mApiClient, FeedPresenter.Type.FEED);
            mHomeFragment.start();
        } else {
            showFragment(mHomeFragment);
            if (!mHomePresenter.hasStarted()) mHomeFragment.start();
        }
        setTitle(R.string.title_home);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mLogoView.setVisibility(View.VISIBLE);
    }

    private void navigateToExplore() {
        if (mExploreFragment == null) {
            mExploreFragment = new PlaceHolderFragment();
            addFragment(mExploreFragment, EXPLORE_TAG);
        } else {
            showFragment(mExploreFragment);
        }

        setTitle(R.string.title_explore);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        mLogoView.setVisibility(View.GONE);
    }

    private void navigateToOpportunities() {
        if (mOpportunitiesFragment == null) {
            mOpportunitiesFragment = FeedFragment.newInstance();
            addFragment(mOpportunitiesFragment, OPPORTUNITIES_TAG);
            mOpportunitiesPresenter = new FeedPresenter(mOpportunitiesFragment, mApiClient, FeedPresenter.Type.OPPORTUNITIES);
            mOpportunitiesFragment.start();
        } else {
            showFragment(mOpportunitiesFragment);
            if (!mOpportunitiesPresenter.hasStarted()) mOpportunitiesFragment.start();
        }
        setTitle(R.string.title_my_opportunties);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        mLogoView.setVisibility(View.GONE);
    }

    private void navigateToVolunteerProfile() {
        if (mVolunteerFragment == null) {
            mVolunteerFragment = VolunteerFragment.newInstance(true);
            AuthHelper authHelper = AuthHelper.getInstance(MainActivity.this);
            mVolunteerPresenter = new VolunteerPresenter(mVolunteerFragment, mApiClient, authHelper, authHelper.getUser().getVolunteer());
            addFragment(mVolunteerFragment, VOLUNTEER_TAG);
        } else {
            showFragment(mVolunteerFragment);
        }

        setTitle(R.string.my_profile);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        mLogoView.setVisibility(View.GONE);
    }

    private void navigateToOrganizationProfile() {
        if (mOrganizationFragment == null) {
            mOrganizationFragment = OrganizationFragment.newInstance(true);
            AuthHelper authHelper = AuthHelper.getInstance(MainActivity.this);
            mOrganizationPresenter = new OrganizationPresenter(mOrganizationFragment, mApiClient, authHelper, authHelper.getUser().getOrganization());
            addFragment(mOrganizationFragment, ORGANIZATION_TAG);
        } else {
            showFragment(mOrganizationFragment);
        }

        setTitle(R.string.my_profile);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        mLogoView.setVisibility(View.GONE);
    }

    private void initializeAndRestoreFragments(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // attempt to restore the fragments
        if (savedInstanceState != null) {

            AuthHelper authHelper = AuthHelper.getInstance(MainActivity.this);

            // restore all section fragments
            mHomeFragment = (FeedFragment) fragmentManager.findFragmentByTag(HOME_TAG);
            if (mHomeFragment != null && mHomePresenter == null) {
                mHomePresenter = new FeedPresenter(mHomeFragment, mApiClient, FeedPresenter.Type.FEED);
            }

            mExploreFragment = (PlaceHolderFragment) fragmentManager.findFragmentByTag(EXPLORE_TAG);

            mOpportunitiesFragment = (FeedFragment) fragmentManager.findFragmentByTag(OPPORTUNITIES_TAG);
            if (mOpportunitiesFragment != null && mOpportunitiesPresenter == null) {
                mOpportunitiesPresenter = new FeedPresenter(mOpportunitiesFragment, mApiClient, FeedPresenter.Type.OPPORTUNITIES);
            }

            mVolunteerFragment = (VolunteerFragment) fragmentManager.findFragmentByTag(VOLUNTEER_TAG);
            if (mVolunteerFragment != null && mVolunteerPresenter == null) {
                mVolunteerPresenter = new VolunteerPresenter(mVolunteerFragment, mApiClient, authHelper, authHelper.getUser().getVolunteer());
            }

            mOrganizationFragment = (OrganizationFragment) fragmentManager.findFragmentByTag(ORGANIZATION_TAG);
            if (mOrganizationFragment != null && mOrganizationPresenter == null) {
                mOrganizationPresenter = new OrganizationPresenter(mOrganizationFragment, mApiClient, authHelper, authHelper.getUser().getOrganization());
            }

            // restore current section Fragment
            if (isCurrentFragment(mHomeFragment)) {
                mCurrentFragment = mHomeFragment;
                mHomeFragment.start();
            } else if (isCurrentFragment(mExploreFragment)) {
                mCurrentFragment = mExploreFragment;
            } else if (isCurrentFragment(mOpportunitiesFragment)) {
                mCurrentFragment = mOpportunitiesFragment;
                mOpportunitiesFragment.start();
            } else if (isCurrentFragment(mVolunteerFragment)) {
                mCurrentFragment = mVolunteerFragment;
            } else if (isCurrentFragment(mOrganizationFragment)) {
                mCurrentFragment = mOrganizationFragment;
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
        hideSectionIfNotCurrent(mExploreFragment, mCurrentFragment, transaction);
        hideSectionIfNotCurrent(mOpportunitiesFragment, mCurrentFragment, transaction);
        hideSectionIfNotCurrent(mVolunteerFragment, mCurrentFragment, transaction);
        hideSectionIfNotCurrent(mOrganizationFragment, mCurrentFragment, transaction);

        transaction.add(R.id.mainFrame, sectionContainerFragment, tag);
        transaction.commitNow();
    }

    private void showFragment(@NonNull Fragment sectionContainerFragment) {
        mCurrentFragment = sectionContainerFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        hideSectionIfNotCurrent(mHomeFragment, mCurrentFragment, transaction);
        hideSectionIfNotCurrent(mExploreFragment, mCurrentFragment, transaction);
        hideSectionIfNotCurrent(mOpportunitiesFragment, mCurrentFragment, transaction);
        hideSectionIfNotCurrent(mVolunteerFragment, mCurrentFragment, transaction);
        hideSectionIfNotCurrent(mOrganizationFragment, mCurrentFragment, transaction);

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
