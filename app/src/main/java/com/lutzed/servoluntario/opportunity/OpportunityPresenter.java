package com.lutzed.servoluntario.opportunity;

import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Cause;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.models.SelectableItem;
import com.lutzed.servoluntario.models.Skill;
import com.lutzed.servoluntario.util.AuthHelper;

import java.util.ArrayList;
import java.util.List;

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
    private Opportunity mOpportunity;
    private List<Cause> causes;
    private List<Long> causeIds;
    private List<Skill> skills;
    private List<Long> skillIds;

    public OpportunityPresenter(OpportunityFragment opportunityFragment, Api.ApiClient apiClient, AuthHelper authHelper, Opportunity opportunity) {
        mView = opportunityFragment;
        mApiClient = apiClient;
        mAuthHelper = authHelper;
        mView.setPresenter(this);

        if (opportunity == null) {
            causes = new ArrayList<>();
            causeIds = new ArrayList<>();
            skills = new ArrayList<>();
            skillIds = new ArrayList<>();
        } else {
            mOpportunity = opportunity;
            causes = mOpportunity.getCauses();
            causeIds = mOpportunity.getCauseIds();
            skills = mOpportunity.getSkills();
            skillIds = mOpportunity.getSkillIds();
        }
    }

    @Override
    public void start() {
        loadContacts();
        if (mOpportunity != null){
            mView.setTitle(mOpportunity.getTitle());
            mView.setDescription(mOpportunity.getDescription());
            mView.setVolunteersNumber(mOpportunity.getVolunteersNumber());
            mView.setTimeCommitment(mOpportunity.getTimeCommitment());
            mView.setOthersRequirements(mOpportunity.getOthersRequirements());
            mView.setTags(mOpportunity.getTags());
            mView.swapCauses(mOpportunity.getCauses());
            mView.swapSkills(mOpportunity.getSkills());
        }

    }

    @Override
    public void attemptCreateOpportunity(String title, String description, Contact contact, List<Long> skillIds, List<Long> causeIds, String volunteersNumber, String timeCommitment, String othersRequirements, String tags) {
        mView.resetErrors();

        boolean isUpdate = mOpportunity != null;

        int volunteersNumberInt = 10;

        Opportunity opportunity = new Opportunity();
        if (isUpdate){
            opportunity.setId(mOpportunity.getId());
        }
        opportunity.setTitle(title);
        opportunity.setDescription(description);
        opportunity.setContactAttributes(contact);
        opportunity.setVolunteersNumber(volunteersNumberInt);
        opportunity.setTimeCommitment(timeCommitment);
        opportunity.setOthersRequirements(othersRequirements);
        opportunity.setCauseIds(causeIds);
        opportunity.setSkillIds(skillIds);
        opportunity.setTags(tags);

        Callback<Opportunity> opportunityCallback = new Callback<Opportunity>() {
            @Override
            public void onResponse(Call<Opportunity> call, Response<Opportunity> response) {

            }

            @Override
            public void onFailure(Call<Opportunity> call, Throwable t) {
                t.printStackTrace();
            }
        };

        if (isUpdate) mApiClient.updateOpportunity(opportunity.getId(), opportunity).enqueue(opportunityCallback);
        else mApiClient.createOpportunity(opportunity).enqueue(opportunityCallback);
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
    public void createNewContact() {
        mView.showCreateNewContact();
    }

    @Override
    public void addNewCause() {
        mView.showAddNewCause(causeIds);
    }

    @Override
    public void addNewSkill() {
        mView.showAddNewSkill(skillIds);
    }

    @Override
    public void onNewSelectableItemsAdded(List<? extends SelectableItem> selectableItems) {
        if (selectableItems == null || selectableItems.isEmpty()) return;

        SelectableItem typeTestItem = selectableItems.get(0);

        if (typeTestItem instanceof Skill) {
            replaceSkills((List<Skill>) selectableItems);
            mView.swapSkills(this.skills);
        } else if (typeTestItem instanceof Cause) {
            replaceCauses((List<Cause>) selectableItems);
            mView.swapCauses(this.causes);
        }
    }

    public void replaceCauses(List<Cause> causes) {
        this.causes.clear();
        this.causeIds.clear();

        for (SelectableItem cause : causes) {
            this.causes.add((Cause) cause);
            this.causeIds.add(cause.getId());
        }
    }

    public void replaceSkills(List<Skill> skills) {
        this.skills.clear();
        this.skillIds.clear();

        for (SelectableItem skill : skills) {
            this.skills.add((Skill) skill);
            this.skillIds.add(skill.getId());
        }
    }

}