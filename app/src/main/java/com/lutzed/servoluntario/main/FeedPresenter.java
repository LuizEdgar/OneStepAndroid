package com.lutzed.servoluntario.main;

import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.FeedItem;
import com.lutzed.servoluntario.models.Opportunity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public class FeedPresenter implements FeedContract.Presenter {

    private final FeedContract.View mView;
    private final Api.ApiClient mApiClient;
    private int mPageToGet;

    public FeedPresenter(FeedFragment opportunityFragment, Api.ApiClient apiClient) {
        mView = opportunityFragment;
        mApiClient = apiClient;
        mView.setPresenter(this);
        mPageToGet = 1;
    }

    @Override
    public void start() {
        loadItems(false);
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
    public void loadItems(final boolean isRefresh) {
        if (isRefresh) mPageToGet = 1;

        mApiClient.getMeFeed(mPageToGet).enqueue(new Callback<List<FeedItem>>() {
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
}
