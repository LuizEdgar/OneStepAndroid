package com.lutzed.servoluntario.main;

import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.FeedItem;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.models.Organization;
import com.lutzed.servoluntario.models.Volunteer;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public class FeedPresenter implements FeedContract.Presenter {

    enum Type {
        FEED, OPPORTUNITIES;
    }

    private final FeedContract.View mView;
    private final Api.ApiClient mApiClient;
    private final Type mType;
    private int mPageToGet;
    private boolean mHasStarted;

    public FeedPresenter(FeedFragment opportunityFragment, Api.ApiClient apiClient, Type type) {
        mView = opportunityFragment;
        mApiClient = apiClient;
        mType = type;
        mView.setPresenter(this);
        mPageToGet = 1;
    }

    @Override
    public void start() {
        loadItems(false);
        mHasStarted = true;
    }

    @Override
    public void refreshItems() {
        loadItems(true);
    }

    @Override
    public void opportunityClicked(Opportunity opportunity) {
        mView.showOpportunity(opportunity);
    }

    @Override
    public void organizationClicked(Organization organization) {
        mView.showOrganization(organization);
    }

    @Override
    public void volunteerClicked(Volunteer volunteer) {
        mView.showVolunteer(volunteer);
    }

    @Override
    public void organizationClicked(Long organizationId) {
        mView.showOrganization(organizationId);
    }

    @Override
    public void volunteerClicked(Long volunteerId) {
        mView.showVolunteer(volunteerId);
    }

    @Override
    public void loadItems(final boolean isRefresh) {
        if (isRefresh) mPageToGet = 1;

        Call<List<FeedItem>> listCall = null;

        switch (mType) {
            case FEED:
                listCall = mApiClient.getMeFeed(mPageToGet);
                break;
            case OPPORTUNITIES:
                listCall = mApiClient.getMeOpportunities(mPageToGet);
                break;
        }

        listCall.enqueue(new Callback<List<FeedItem>>() {
            @Override
            public void onResponse(Call<List<FeedItem>> call, Response<List<FeedItem>> response) {
                if (isRefresh) {
                    mView.swapItems(response.body());
                } else {
                    mView.addItems(response.body());
                }
                mPageToGet++;
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onFailure(Call<List<FeedItem>> call, Throwable t) {
                mView.setLoadingIndicator(false);
                mView.showLoadingError();
            }
        });
    }

    @Override
    public boolean hasStarted() {
        return mHasStarted;
    }
}
