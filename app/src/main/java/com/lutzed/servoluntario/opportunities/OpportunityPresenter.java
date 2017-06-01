package com.lutzed.servoluntario.opportunities;

import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.util.AuthHelper;
import com.lutzed.servoluntario.util.DateHelper;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public class OpportunityPresenter implements OpportunityContract.Presenter {

    private final OpportunityContract.View mView;
    private final Api.ApiClient mApiClient;
    private final AuthHelper mAuthHelper;
    private final Long mOpportunityId;
    private Opportunity mOpportunity;

    public OpportunityPresenter(OpportunityFragment opportunityFragment, Api.ApiClient apiClient, AuthHelper authHelper, Opportunity opportunity) {
        mView = opportunityFragment;
        mApiClient = apiClient;
        mAuthHelper = authHelper;
        mView.setPresenter(this);
        mOpportunity = opportunity;
        mOpportunityId = opportunity.getId();
    }

    public OpportunityPresenter(OpportunityFragment opportunityFragment, Api.ApiClient apiClient, AuthHelper authHelper, Long opportunityId) {
        mView = opportunityFragment;
        mApiClient = apiClient;
        mAuthHelper = authHelper;
        mView.setPresenter(this);
        mOpportunityId = opportunityId;
    }

    @Override
    public void start() {
        if (mOpportunity != null) {
            setOpportunityDateOnView(mOpportunity);
        } else {
            loadOpportunity();
        }
    }

    @Override
    public void loadOpportunity() {
        mApiClient.getOpportunity(mOpportunityId).enqueue(new Callback<Opportunity>() {
            @Override
            public void onResponse(Call<Opportunity> call, Response<Opportunity> response) {
                setOpportunityDateOnView(response.body());
            }

            @Override
            public void onFailure(Call<Opportunity> call, Throwable t) {

            }
        });
    }

    private void setOpportunityDateOnView(Opportunity opportunity) {
        mView.setTitle(opportunity.getTitle());
        mView.setDescription(opportunity.getDescription());
        mView.setVolunteersNumber(opportunity.getVolunteersNumber());
        mView.setTimeCommitment(opportunity.getTimeCommitment());
        mView.setOthersRequirements(opportunity.getOthersRequirements());
        mView.setTags(opportunity.getTags());
        mView.addCauses(opportunity.getCauses());
        mView.addSkills(opportunity.getSkills());
        mView.setCreator(opportunity.getOpportunitable().getName());
        mView.addImages(opportunity.getImages());
        mView.setContact(opportunity.getContact());

        if (!opportunity.getVirtual()) {
            if (opportunity.getLocation() != null) {
                mView.setLocation(opportunity.getLocation().getName());
            }
        } else {
            mView.setLocationToVirtual();
        }

        if (!opportunity.getOngoing()) {
            String time;

            Date startTime = DateHelper.deserialize(opportunity.getStartAt());
            if (opportunity.getStartTimeSet())
                time = DateHelper.format(DateHelper.eventDatetimeFormat, startTime);
            else time = DateHelper.format(DateHelper.eventDateFormat, startTime);

            Date endTime = DateHelper.deserialize(opportunity.getStartAt());
            if (opportunity.getEndDateSet() && endTime != null) {
                if (opportunity.getEndTimeSet())
                    time += " - " + DateHelper.format(DateHelper.eventDatetimeFormat, endTime);
                else time += " - " + DateHelper.format(DateHelper.eventDateFormat, endTime);
            }

            mView.setTime(time);
        } else {
            mView.setTimeToOngoing();
        }
    }
}
