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
import java.util.Random;

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
    private List<Contact> mContacts;

    public OpportunityPresenter(OpportunityFragment opportunityFragment, Api.ApiClient apiClient, AuthHelper authHelper, Opportunity opportunity) {
        mView = opportunityFragment;
        mApiClient = apiClient;
        mAuthHelper = authHelper;
        mView.setPresenter(this);
        mOpportunity = opportunity;
        mContacts = new ArrayList<>();
    }

    @Override
    public void start() {
        if (mOpportunity != null) {
            mView.setTitle(mOpportunity.getTitle());
            mView.setDescription(mOpportunity.getDescription());
            mView.setVolunteersNumber(mOpportunity.getVolunteersNumber());
            mView.setTimeCommitment(mOpportunity.getTimeCommitment());
            mView.setOthersRequirements(mOpportunity.getOthersRequirements());
            mView.setTags(mOpportunity.getTags());
            mView.addUniqueCauses(mOpportunity.getCauses(), null);
            mView.addUniqueSkills(mOpportunity.getSkills(), null);
            Contact contact = mOpportunity.getContact();
            if (contact != null){
                ArrayList<Contact> contacts = new ArrayList<>();
                contacts.add(contact);
                mView.setContacts(contacts, contact.getId());
            }
        }
        loadContacts();
    }

    @Override
    public void attemptCreateOpportunity(String title, String description, Contact contact, List<Long> skillIds, List<Long> causeIds, String volunteersNumber, String timeCommitment, String othersRequirements, String tags) {
        mView.resetErrors();

        boolean isUpdate = mOpportunity != null;

        int volunteersNumberInt = 10;

        Opportunity opportunity = new Opportunity();
        if (isUpdate) {
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

        if (isUpdate)
            mApiClient.updateOpportunity(opportunity.getId(), opportunity).enqueue(opportunityCallback);
        else mApiClient.createOpportunity(opportunity).enqueue(opportunityCallback);
    }

    @Override
    public void loadContacts() {
        mApiClient.getMeContacts().enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                mContacts = response.body();
                if (mOpportunity != null) {
                    Contact contact = mOpportunity.getContact();
                    if (contact != null) {
                        int index = Contact.containsIndentiq(mContacts, contact);
                        if (index < 0){
                            mContacts.add(contact);
                        }else{
                            contact = mContacts.get(index);
                        }
                        mView.setContacts(mContacts, contact.getId());
                    } else {
                        mView.setContacts(mContacts, null);
                    }
                } else {
                    mView.setContacts(mContacts, null);
                }
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {

            }
        });
    }

    @Override
    public void addNewContact(String name, String phone, String email) {
        Contact contact = new Contact(name, phone, email);
        contact.setId(new Random().nextLong());
        mContacts.add(contact);
        mView.setContacts(mContacts, contact.getId());
    }

    @Override
    public void createNewContact() {
        mView.showCreateNewContact();
    }

    @Override
    public void addNewCause(List<Long> existingCauseIds) {
        mView.showAddNewCause(existingCauseIds);
    }

    @Override
    public void addNewSkill(List<Long> existingSkillIds) {
        mView.showAddNewSkill(existingSkillIds);
    }

    @Override
    public void onNewItemsSelection(ArrayList<SelectableItem> selectedItems, ArrayList<SelectableItem> notSelectedItems) {
        SelectableItem typeTestItem = null;

        if (selectedItems != null && !selectedItems.isEmpty()) {
            typeTestItem = selectedItems.get(0);
        } else {
            if (notSelectedItems == null || notSelectedItems.isEmpty()) return;
            typeTestItem = notSelectedItems.get(0);
        }

        if (typeTestItem instanceof Skill) {
            mView.addUniqueSkills(selectedItems, notSelectedItems);
        } else if (typeTestItem instanceof Cause) {
            mView.addUniqueCauses(selectedItems, notSelectedItems);
        }
    }

}