package com.lutzed.servoluntario.selection;

import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Skill;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public class SkillsSelectionPresenter implements ItemsSelectionContract.Presenter {

    private final ItemsSelectionContract.View mView;
    private final Api.ApiClient mApiClient;

    public SkillsSelectionPresenter(ItemsSelectionFragment loginFragment, Api.ApiClient apiClient) {
        mView = loginFragment;
        mApiClient = apiClient;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadItems(false);
    }

    @Override
    public void loadItems(final boolean isRefresh) {
        mApiClient.getSkills().enqueue(new Callback<List<Skill>>() {
            @Override
            public void onResponse(Call<List<Skill>> call, Response<List<Skill>> response) {
                mView.showItems(response.body(), isRefresh);
            }

            @Override
            public void onFailure(Call<List<Skill>> call, Throwable t) {
                mView.showLoadingError();
            }
        });
    }

    @Override
    public void saveSelection() {

    }
}
