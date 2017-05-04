package com.lutzed.servoluntario.completion;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Organization;
import com.lutzed.servoluntario.models.User;
import com.lutzed.servoluntario.util.AuthHelper;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public class OrganizationCompletionPresenter implements OrganizationCompletionContract.Presenter {

    private final OrganizationCompletionContract.View mView;
    private final Api.ApiClient mApiClient;
    private final User mUser;
    private final AuthHelper mAuthHelper;

    public OrganizationCompletionPresenter(OrganizationCompletionFragment volunteerCompletionFragment, Api.ApiClient apiClient, AuthHelper authHelper) {
        mView = volunteerCompletionFragment;
        mApiClient = apiClient;
        mAuthHelper = authHelper;
        mUser = authHelper.getUser();
        mView.setPresenter(this);
    }

    @Override
    public void saveProfile(String about, String need, String goal, String site) {
        mView.resetErrors();

        User user = new User();
        user.setId(mUser.getId());

        Organization organizationAttributes = new Organization();
        organizationAttributes.setId(mUser.getOrganization().getId());
        organizationAttributes.setAbout(about);
        organizationAttributes.setNeed(need);
        organizationAttributes.setGoal(goal);
        organizationAttributes.setSite(site);
        user.setOrganizationAttributes(organizationAttributes);

        mView.setLoadingIndicator(true);
        mApiClient.updateUser(user.getId(), user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                mView.setLoadingIndicator(false);
                switch (response.code()) {
                    case 200:
                        mAuthHelper.setUser(response.body());
                        mView.navigateToChooseSkills();
                        break;
                    case 422:
                        mView.showDefaultSaveError();
                        break;
                    default:
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mView.setLoadingIndicator(false);
                mView.showDefaultSaveError();
            }
        });
    }

    @Override
    public void start() {
        populateUserData();
    }

    private void populateUserData() {
        User user = mAuthHelper.getUser();
        Organization organization = user.getOrganization();
        mView.setAboutField(organization.getAbout());
        mView.setNeedField(organization.getNeed());
        mView.setSiteField(organization.getSite());
        mView.setGoalField(organization.getGoal());
    }

    private void populateFacebookData() {
        mView.setLoadingIndicator(true);
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject me,
                            GraphResponse response) {
                        mView.setLoadingIndicator(false);
//                        mView.populateFacebookFields(me.optString("name"), me.optString("email"));
//                        mGender = me.optString("gender");
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

}