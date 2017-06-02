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
import com.lutzed.servoluntario.models.User;
import com.lutzed.servoluntario.opportunities.EditOpportunityActivity;
import com.lutzed.servoluntario.organization.OrganizationFragment;
import com.lutzed.servoluntario.organization.OrganizationPresenter;
import com.lutzed.servoluntario.user.EditUserFragment;
import com.lutzed.servoluntario.user.EditUserPresenter;
import com.lutzed.servoluntario.util.AuthHelper;
import com.lutzed.servoluntario.volunteer.VolunteerFragment;
import com.lutzed.servoluntario.volunteer.VolunteerPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String HOME_TAG = "home_tag";
    private static final String DO_TAG = "do_tag";
    private static final String EDIT_PROFILE_TAG = "edit_profile_tag";
    private static final String VOLUNTEER_TAG = "volunteer_tag";
    private static final String ORGANIZATION_TAG = "organization_tag";

    private Fragment mCurrentFragment;

    private Api.ApiClient mApiClient;

    private FeedFragment mHomeFragment;
    private FeedPresenter mHomePresenter;
    private EditUserPresenter mEditProfilePresenter;
    private EditUserFragment mEditProfileFragment;
    private VolunteerPresenter mVolunteerPresenter;
    private VolunteerFragment mVolunteerFragment;
    private OrganizationPresenter mOrganizationPresenter;
    private OrganizationFragment mOrganizationFragment;

    private PlaceHolderFragment mDoFragment;
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

                    Api.getClient(AuthHelper.getInstance(MainActivity.this).getUser()).getOpportunity(11l).enqueue(new Callback<Opportunity>() {
                        @Override
                        public void onResponse(Call<Opportunity> call, Response<Opportunity> response) {
                            Intent intent = new Intent(MainActivity.this, EditOpportunityActivity.class);
                            intent.putExtra(EditOpportunityActivity.EXTRA_OPPORTUNITY, response.body());
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Opportunity> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

                    break;
                case R.id.navigation_profile:
                    if (AuthHelper.getInstance(MainActivity.this).getUser().getKindEnum() == User.Kind.VOLUNTEER) {
                        navigateToVolunteerProfile();
                    } else {
                        navigateToOrganizationProfile();
                    }
//                    navigateToEditProfile();
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

        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void navigateToEditProfile() {
        if (mEditProfileFragment == null) {
            mEditProfileFragment = EditUserFragment.newInstance();
            mEditProfilePresenter = new EditUserPresenter(mEditProfileFragment, mApiClient, AuthHelper.getInstance(MainActivity.this));
            addFragment(mEditProfileFragment, EDIT_PROFILE_TAG);
        } else {
            showFragment(mEditProfileFragment);
        }
    }

    private void navigateToVolunteerProfile() {
        if (mVolunteerFragment == null) {
            mVolunteerFragment = VolunteerFragment.newInstance(true);
            AuthHelper authHelper = AuthHelper.getInstance(MainActivity.this);
            mVolunteerPresenter = new VolunteerPresenter(mVolunteerFragment, mApiClient, authHelper, authHelper.getUser().getVolunteer());
            addFragment(mVolunteerFragment, VOLUNTEER_TAG);
//            mVolunteerFragment.start();
        } else {
            showFragment(mVolunteerFragment);
        }
    }

    private void navigateToOrganizationProfile() {
        if (mOrganizationFragment == null) {
            mOrganizationFragment = OrganizationFragment.newInstance(true);
            AuthHelper authHelper = AuthHelper.getInstance(MainActivity.this);
            mOrganizationPresenter = new OrganizationPresenter(mOrganizationFragment, mApiClient, authHelper, authHelper.getUser().getOrganization());
            addFragment(mOrganizationFragment, ORGANIZATION_TAG);
//            mOrganizationFragment.start();
        } else {
            showFragment(mOrganizationFragment);
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
            if (!mHomePresenter.hasStarted()) mHomeFragment.start();
        }
    }

    private void initializeAndRestoreFragments(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // attempt to restore the fragments
        if (savedInstanceState != null) {

            AuthHelper authHelper = AuthHelper.getInstance(MainActivity.this);

            // restore all section fragments
            mHomeFragment = (FeedFragment) fragmentManager.findFragmentByTag(HOME_TAG);
            if (mHomeFragment != null && mHomePresenter == null) {
                mHomePresenter = new FeedPresenter(mHomeFragment, mApiClient);
            }

            mDoFragment = (PlaceHolderFragment) fragmentManager.findFragmentByTag(DO_TAG);

            mEditProfileFragment = (EditUserFragment) fragmentManager.findFragmentByTag(EDIT_PROFILE_TAG);
            if (mEditProfileFragment != null && mHomePresenter == null) {
                mEditProfilePresenter = new EditUserPresenter(mEditProfileFragment, mApiClient, authHelper);
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
            } else if (isCurrentFragment(mDoFragment)) {
                mCurrentFragment = mDoFragment;
            } else if (isCurrentFragment(mEditProfileFragment)) {
                mCurrentFragment = mEditProfileFragment;
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
//            navigateToEditProfile();
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
        hideSectionIfNotCurrent(mEditProfileFragment, mCurrentFragment, transaction);
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
        hideSectionIfNotCurrent(mDoFragment, mCurrentFragment, transaction);
        hideSectionIfNotCurrent(mEditProfileFragment, mCurrentFragment, transaction);
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
