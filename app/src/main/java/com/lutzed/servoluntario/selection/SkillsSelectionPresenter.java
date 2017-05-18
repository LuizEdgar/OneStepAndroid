package com.lutzed.servoluntario.selection;

import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Organization;
import com.lutzed.servoluntario.models.Skill;
import com.lutzed.servoluntario.models.User;
import com.lutzed.servoluntario.models.Volunteer;
import com.lutzed.servoluntario.util.AuthHelper;

import java.util.Iterator;
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
    private final ItemsSelectionActivity.Mode mSelectionMode;
    private final List<Long> mItemsToExclude;
    private final AuthHelper mAuthHelper;
    private int mPageToGet;
    private List<Long> mSelectedSkills;

    public SkillsSelectionPresenter(ItemsSelectionFragment loginFragment, Api.ApiClient apiClient, AuthHelper authHelper, ItemsSelectionActivity.Mode mode, List<Long> itemsToExclude) {
        mView = loginFragment;
        mApiClient = apiClient;
        mSelectionMode = mode;
        mItemsToExclude = itemsToExclude;
        mView.setPresenter(this);
        mPageToGet = 1;
        mAuthHelper = authHelper;
        mSelectedSkills = mAuthHelper.getUser().getSkillsIds();
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

        mApiClient.getSkills(mPageToGet).enqueue(new Callback<List<Skill>>() {
            @Override
            public void onResponse(Call<List<Skill>> call, Response<List<Skill>> response) {

                boolean isMulti = mSelectionMode == ItemsSelectionActivity.Mode.MULTIPLE;
                boolean hasItemsToExclude = !mItemsToExclude.isEmpty();

                if (isMulti || hasItemsToExclude) {
                    for (Iterator<Skill> iterator = response.body().iterator(); iterator.hasNext(); ) {
                        Skill skill = iterator.next();
                        if (isMulti) skill.setSelected(mSelectedSkills.contains(skill.getId()));
                        if (hasItemsToExclude && mItemsToExclude.contains(skill.getId())) iterator.remove();
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
            public void onFailure(Call<List<Skill>> call, Throwable t) {
                mView.setLoadingIndicator(false);
                mView.showLoadingError();
            }
        });
    }

    @Override
    public void saveItems(List<Long> selectedIds) {

        User user = new User();
        user.setId(mAuthHelper.getUser().getId());

        switch (mAuthHelper.getUser().getKindEnum()) {
            case VOLUNTEER:
                Volunteer volunteerAttributes = new Volunteer();
                volunteerAttributes.setId(mAuthHelper.getUser().getVolunteer().getId());
                volunteerAttributes.setSkillIds(selectedIds);
                user.setVolunteerAttributes(volunteerAttributes);
                break;
            case ORGANIZATION:
                Organization organizationAttributes = new Organization();
                organizationAttributes.setId(mAuthHelper.getUser().getOrganization().getId());
                organizationAttributes.setSkillIds(selectedIds);
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
                        mView.navigateToChooseCauses();
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
