package com.lutzed.servoluntario.opportunity;

import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Cause;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.models.Skill;
import com.lutzed.servoluntario.util.AuthHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public class CreateOpportunityPresenter implements CreateOpportunityContract.Presenter {

    private final CreateOpportunityContract.View mView;
    private final Api.ApiClient mApiClient;
    private final AuthHelper mAuthHelper;
    private final Opportunity mOpportunity;

    public CreateOpportunityPresenter(CreateOpportunityFragment createOpportunityFragment, Api.ApiClient apiClient, AuthHelper authHelper) {
        this(createOpportunityFragment, apiClient, authHelper, null);
    }

    public CreateOpportunityPresenter(CreateOpportunityFragment createOpportunityFragment, Api.ApiClient apiClient, AuthHelper authHelper, Opportunity opportunity) {
        mView = createOpportunityFragment;
        mApiClient = apiClient;
        mAuthHelper = authHelper;
        mOpportunity = opportunity;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadContacts();
        loadCauses();
        loadSkills();
    }

    @Override
    public void attemptCreateOpportunity() {
        mView.resetErrors();

    }

    @Override
    public void loadContacts() {
        mApiClient.getMeContacts().enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                mView.setContacts(response.body(), null);
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {

            }
        });
    }

    @Override
    public void loadSkills() {
        mApiClient.getMeSkills().enqueue(new Callback<List<Skill>>() {
            @Override
            public void onResponse(Call<List<Skill>> call, Response<List<Skill>> response) {
                mView.setSkills(response.body());
            }

            @Override
            public void onFailure(Call<List<Skill>> call, Throwable t) {

            }
        });
    }

    @Override
    public void loadCauses() {
        mApiClient.getMeCauses().enqueue(new Callback<List<Cause>>() {
            @Override
            public void onResponse(Call<List<Cause>> call, Response<List<Cause>> response) {
                mView.setCauses(response.body());
            }

            @Override
            public void onFailure(Call<List<Cause>> call, Throwable t) {

            }
        });
    }

    @Override
    public void createNewContact() {
        mView.showCreateNewContact();
    }

    @Override
    public void addNewCause() {
        mView.showAddNewCause();
    }

    @Override
    public void addNewSkill() {
        mView.showAddNewSkill();
    }

}