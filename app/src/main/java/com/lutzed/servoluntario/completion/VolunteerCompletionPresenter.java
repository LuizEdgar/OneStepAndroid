package com.lutzed.servoluntario.completion;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Image;
import com.lutzed.servoluntario.models.User;
import com.lutzed.servoluntario.models.Volunteer;
import com.lutzed.servoluntario.util.AuthHelper;
import com.lutzed.servoluntario.util.Snippets;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public class VolunteerCompletionPresenter implements VolunteerCompletionContract.Presenter {

    private final VolunteerCompletionContract.View mView;
    private final Api.ApiClient mApiClient;
    private final User mUser;
    private final AuthHelper mAuthHelper;
    private Image mProfileImage;
    private String mGender;

    public VolunteerCompletionPresenter(VolunteerCompletionFragment volunteerCompletionFragment, Api.ApiClient apiClient, AuthHelper authHelper) {
        mView = volunteerCompletionFragment;
        mApiClient = apiClient;
        mAuthHelper = authHelper;
        mUser = authHelper.getUser();
        mView.setPresenter(this);
    }

    @Override
    public void saveProfile(String about, String occupation) {
        mView.resetErrors();

        User user = new User();
        user.setId(mUser.getId());

        Volunteer volunteerAttributes = new Volunteer();
        volunteerAttributes.setId(mUser.getVolunteer().getId());
        volunteerAttributes.setAbout(about);
        volunteerAttributes.setOccupation(occupation);
        if (mProfileImage != null && mProfileImage.getBitmap() != null) {
            volunteerAttributes.setProfileImage64(Snippets.encodeToBase64(mProfileImage.getBitmap(), true));
        }
        user.setVolunteerAttributes(volunteerAttributes);

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
        Volunteer volunteer = user.getVolunteer();
        mView.setAboutField(volunteer.getAbout());
        mView.setOccupationField(volunteer.getOccupation());

        if (volunteer.getProfileImage() != null) {
            mProfileImage = volunteer.getProfileImage();
            mView.setProfileImage(mProfileImage.getUrl());
        }

        if (user.getFacebookId() != null) {
            populateFacebookData();
        }
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

                        if (me.has("picture")) {
                            String profilePicUrl = null;
                            try {
                                profilePicUrl = me.getJSONObject("picture").getJSONObject("data").getString("url");
                                mView.setProfileImage(profilePicUrl);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        mGender = me.optString("gender");
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, gender, picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void addNewProfileImage() {
        mView.showProfileImageTypePicker();
    }

    @Override
    public void addNewImageFromCamera(int request) {
        mView.showAddNewImageFromCamera(request);
    }

    @Override
    public void addNewImageFromGallery(int request) {
        mView.showAddNewImageFromGallery(request);
    }

    @Override
    public void onNewProfileImageAdded(Bitmap bitmap) {
        mProfileImage = new Image(bitmap);
        mView.setProfileImage(bitmap);
    }

}
