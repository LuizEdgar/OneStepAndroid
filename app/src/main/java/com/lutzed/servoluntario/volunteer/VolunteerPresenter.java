package com.lutzed.servoluntario.volunteer;

import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Location;
import com.lutzed.servoluntario.models.Volunteer;
import com.lutzed.servoluntario.util.AuthHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public class VolunteerPresenter implements VolunteerContract.Presenter {

    private final VolunteerContract.View mView;
    private final Api.ApiClient mApiClient;
    private final AuthHelper mAuthHelper;
    private final Long mVolunteerId;
    private Volunteer mOpportunity;

    public VolunteerPresenter(VolunteerFragment volunteerFragment, Api.ApiClient apiClient, AuthHelper authHelper, Volunteer volunteer) {
        mView = volunteerFragment;
        mApiClient = apiClient;
        mAuthHelper = authHelper;
        mView.setPresenter(this);
        mOpportunity = volunteer;
        mVolunteerId = volunteer.getId();
    }

    public VolunteerPresenter(VolunteerFragment volunteerFragment, Api.ApiClient apiClient, AuthHelper authHelper, Long volunteerId) {
        mView = volunteerFragment;
        mApiClient = apiClient;
        mAuthHelper = authHelper;
        mView.setPresenter(this);
        mVolunteerId = volunteerId;
    }

    @Override
    public void start() {
        if (mOpportunity != null) {
            setVolunteerDateOnView(mOpportunity);
        } else {
            loadOpportunity();
        }
    }

    @Override
    public void loadOpportunity() {
        mApiClient.getVolunteer(mVolunteerId).enqueue(new Callback<Volunteer>() {
            @Override
            public void onResponse(Call<Volunteer> call, Response<Volunteer> response) {
                setVolunteerDateOnView(response.body());
            }

            @Override
            public void onFailure(Call<Volunteer> call, Throwable t) {

            }
        });
    }

    @Override
    public void onEditVolunteerClicked() {
        mView.showEditVolunteer();
    }

    @Override
    public void signOut() {
        mAuthHelper.signout();
        mView.signOut();
    }

    private void setVolunteerDateOnView(Volunteer volunteer) {
        mView.setName(volunteer.getName());
        mView.setAbout(volunteer.getAbout());
        mView.addCauses(volunteer.getCauses());
        mView.addSkills(volunteer.getSkills());
        if (volunteer.getProfileImage() != null)
            mView.setCoverImage(volunteer.getProfileImage().getUrl());
        mView.setContacts(volunteer.getContacts());
        List<Location> locations = volunteer.getLocations();
        if (locations != null && !locations.isEmpty()) mView.setLocation(locations.get(0).getName());
    }
}
