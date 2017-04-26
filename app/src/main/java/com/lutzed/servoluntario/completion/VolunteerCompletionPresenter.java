package com.lutzed.servoluntario.completion;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.util.AuthHelper;

import org.json.JSONObject;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public class VolunteerCompletionPresenter implements VolunteerCompletionContract.Presenter {

    private final VolunteerCompletionContract.View mView;
    private final Api.ApiClient mApiClient;
    private final AuthHelper mAuthHelper;
    private String mGender;

    public VolunteerCompletionPresenter(VolunteerCompletionFragment volunteerCompletionFragment, Api.ApiClient apiClient, AuthHelper authHelper) {
        mView = volunteerCompletionFragment;
        mApiClient = apiClient;
        mAuthHelper = authHelper;
        mView.setPresenter(this);
    }

    @Override
    public void saveProfile() {
        mView.resetErrors();
    }

    @Override
    public void start() {
        populateFacebookData();
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
                        mView.populateFacebookFields(me.optString("name"), me.optString("email"));
                        mGender = me.optString("gender");
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
