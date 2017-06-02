package com.lutzed.servoluntario.organization;

import android.text.TextUtils;

import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Location;
import com.lutzed.servoluntario.models.Organization;
import com.lutzed.servoluntario.util.AuthHelper;
import com.lutzed.servoluntario.util.DateHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public class OrganizationPresenter implements OrganizationContract.Presenter {

    private final OrganizationContract.View mView;
    private final Api.ApiClient mApiClient;
    private final AuthHelper mAuthHelper;
    private final Long mOrganizationId;
    private Organization mOpportunity;

    public OrganizationPresenter(OrganizationFragment organizationFragment, Api.ApiClient apiClient, AuthHelper authHelper, Organization organization) {
        mView = organizationFragment;
        mApiClient = apiClient;
        mAuthHelper = authHelper;
        mView.setPresenter(this);
        mOpportunity = organization;
        mOrganizationId = organization.getId();
    }

    public OrganizationPresenter(OrganizationFragment organizationFragment, Api.ApiClient apiClient, AuthHelper authHelper, Long organizationId) {
        mView = organizationFragment;
        mApiClient = apiClient;
        mAuthHelper = authHelper;
        mView.setPresenter(this);
        mOrganizationId = organizationId;
    }

    @Override
    public void start() {
        if (mOpportunity != null) {
            setOrganizationDateOnView(mOpportunity);
        } else {
            loadOpportunity();
        }
    }

    @Override
    public void loadOpportunity() {
        mApiClient.getOrganization(mOrganizationId).enqueue(new Callback<Organization>() {
            @Override
            public void onResponse(Call<Organization> call, Response<Organization> response) {
                setOrganizationDateOnView(response.body());
            }

            @Override
            public void onFailure(Call<Organization> call, Throwable t) {

            }
        });
    }

    @Override
    public void onEditOrganizationClicked() {
        mView.showEditOrganization();
    }

    @Override
    public void signOut() {
        mAuthHelper.signout();
        mView.signOut();
    }

    private void setOrganizationDateOnView(Organization organization) {
        mView.setName(organization.getName());
        mView.setAbout(organization.getAbout());
        mView.addCauses(organization.getCauses());
        mView.addSkills(organization.getSkills());
        mView.addImages(organization.getImages());
        if (organization.getProfileImage() != null)
            mView.setCoverImage(organization.getProfileImage().getUrl());
        mView.setMission(organization.getMission());
        mView.setCnpj(organization.getCnpj());
        mView.setContacts(organization.getContacts());
        List<Location> locations = organization.getLocations();
        if (locations != null && !locations.isEmpty())
            mView.setLocation(locations.get(0).getName());
        String establishedAt = organization.getEstablishedAt();
        if (!TextUtils.isEmpty(establishedAt))
            mView.setEstablishedAt(DateHelper.format(DateHelper.yearFormat, establishedAt));

    }
}