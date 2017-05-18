package com.lutzed.servoluntario.selection;

import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Cause;
import com.lutzed.servoluntario.models.Organization;
import com.lutzed.servoluntario.models.User;
import com.lutzed.servoluntario.models.Volunteer;
import com.lutzed.servoluntario.util.AuthHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public class CauseSelectionPresenter implements ItemsSelectionContract.Presenter {

    private final ItemsSelectionContract.View mView;
    private final Api.ApiClient mApiClient;
    private final ItemsSelectionActivity.Mode mSelectionMode;
    private final AuthHelper mAuthHelper;
    private int mPageToGet;
    private final List<Long> mItemsToExclude;
    private HashSet<Long> mSelectedCausesIds;

    public CauseSelectionPresenter(ItemsSelectionFragment loginFragment, Api.ApiClient apiClient, AuthHelper authHelper, ItemsSelectionActivity.Mode mode, List<Long> itemsToExclude, ArrayList<Long> itensToCheck) {
        mView = loginFragment;
        mApiClient = apiClient;
        mSelectionMode = mode;
        mItemsToExclude = itemsToExclude;
        mView.setPresenter(this);
        mPageToGet = 1;
        mAuthHelper = authHelper;
        mSelectedCausesIds = new HashSet<>();
        if (itensToCheck != null) {
            mSelectedCausesIds.addAll(itensToCheck);
        }
        if (mode == ItemsSelectionActivity.Mode.MULTIPLE_SAVE_TO_USER){
            mSelectedCausesIds.addAll(mAuthHelper.getUser().getCauseIds());
        }
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
    public void loadItems(final boolean isRefresh) {
        if (isRefresh) mPageToGet = 1;

        mApiClient.getCauses(mPageToGet).enqueue(new Callback<List<Cause>>() {
            @Override
            public void onResponse(Call<List<Cause>> call, Response<List<Cause>> response) {

                boolean isMulti = mSelectionMode != ItemsSelectionActivity.Mode.SINGLE_SELECTION;
                boolean hasItemsToExclude = !mItemsToExclude.isEmpty();

                if (isMulti || hasItemsToExclude) {
                    for (Iterator<Cause> iterator = response.body().iterator(); iterator.hasNext(); ) {
                        Cause cause = iterator.next();
                        if (isMulti) cause.setSelected(mSelectedCausesIds.contains(cause.getId()));
                        if (hasItemsToExclude && mItemsToExclude.contains(cause.getId()))
                            iterator.remove();
                    }
                }

                if (isRefresh) {
                    mView.swapItems(response.body());
                } else {
                    mView.addItems(response.body());
                }
                mPageToGet++;
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onFailure(Call<List<Cause>> call, Throwable t) {
                mView.setLoadingIndicator(false);
                mView.showLoadingError();
            }
        });
    }

    @Override
    public void saveItemsToUser(List<Long> selectedIds) {

        User user = new User();
        user.setId(mAuthHelper.getUser().getId());

        switch (mAuthHelper.getUser().getKindEnum()) {
            case VOLUNTEER:
                Volunteer volunteerAttributes = new Volunteer();
                volunteerAttributes.setId(mAuthHelper.getUser().getVolunteer().getId());
                volunteerAttributes.setCauseIds(selectedIds);
                user.setVolunteerAttributes(volunteerAttributes);
                break;
            case ORGANIZATION:
                Organization organizationAttributes = new Organization();
                organizationAttributes.setId(mAuthHelper.getUser().getOrganization().getId());
                organizationAttributes.setCauseIds(selectedIds);
                user.setOrganizationAttributes(organizationAttributes);
                break;
        }

        mView.setSavingIndicator(true);
        mApiClient.updateUser(user.getId(), user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                mView.setSavingIndicator(false);
                switch (response.code()) {
                    case 200:
                        mAuthHelper.setUser(response.body());
                        mView.navigateToMain();
                        break;
                    case 422:
                        mView.showDefaultSaveError();
                        break;
                    default:
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mView.setSavingIndicator(false);
                mView.showDefaultSaveError();
            }
        });

    }
}
