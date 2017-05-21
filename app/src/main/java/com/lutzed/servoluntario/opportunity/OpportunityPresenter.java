package com.lutzed.servoluntario.opportunity;

import com.google.android.gms.location.places.Place;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Cause;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.models.SelectableItem;
import com.lutzed.servoluntario.models.Skill;
import com.lutzed.servoluntario.util.AuthHelper;
import com.lutzed.servoluntario.util.DateHelper;

import java.util.ArrayList;
import java.util.Calendar;
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
    private Place mCurrentPlace;
    private Calendar mStartAt;
    private Calendar mEndAt;
    private boolean mStartDateSet;
    private boolean mEndDateSet;
    private boolean mStartTimeSet;
    private boolean mEndTimeSet;
    private boolean mIsOngoing;
    private boolean mIsVirtual;
//    private DateHolder mStartDateHolder;
//    private DateHolder mEndDateHolder;
//    private TimeHolder mStartTimeHolder;
//    private TimeHolder mEndTimeHolder;

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
        Calendar now = Calendar.getInstance();

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
            if (contact != null) {
                ArrayList<Contact> contacts = new ArrayList<>();
                contacts.add(contact);
                mView.setContacts(contacts, contact.getId());
            }

            if (mOpportunity.getStartAt() != null && !mOpportunity.getOngoing()) {
                mStartAt = DateHelper.deserializeToCalendar(mOpportunity.getStartAt());
            } else {
                mStartAt = Calendar.getInstance();
            }
            if (mOpportunity.getEndAt() != null && !mOpportunity.getOngoing()) {
                mEndAt = DateHelper.deserializeToCalendar(mOpportunity.getEndAt());
            } else {
                mEndAt = Calendar.getInstance();
            }

            mIsOngoing = mOpportunity.getOngoing();
            if (!mOpportunity.getOngoing()) {
                mView.setTimeGroupType(OpportunityFragment.TimeType.DATED);
                mStartDateSet = mOpportunity.getStartDateSet();
                if (mOpportunity.getStartDateSet()) {
                    mView.setStartDate(DateHelper.format(DateHelper.dateFormat, mOpportunity.getStartAt()));
                }
                mEndDateSet = mOpportunity.getEndDateSet();
                if (mOpportunity.getEndDateSet()) {
                    mView.setEndDate(DateHelper.format(DateHelper.dateFormat, mOpportunity.getEndAt()));
                }
                mStartTimeSet = mOpportunity.getStartTimeSet();
                if (mOpportunity.getStartTimeSet()) {
                    mView.setStartTime(DateHelper.format(DateHelper.timeFormat, mOpportunity.getStartAt()));
                }
                mEndTimeSet = mOpportunity.getEndTimeSet();
                if (mOpportunity.getEndTimeSet()) {
                    mView.setEndTime(DateHelper.format(DateHelper.timeFormat, mOpportunity.getEndAt()));
                }
            } else {
                mView.setTimeGroupType(OpportunityFragment.TimeType.ONGOING);
            }

        } else {
            mStartAt = Calendar.getInstance();
            mEndAt = Calendar.getInstance();
        }

        loadContacts();
    }

    @Override
    public void attemptCreateOpportunity(String title, String description, Contact contact, List<Long> skillIds, List<Long> causeIds, String volunteersNumber, String timeCommitment, String othersRequirements, String tags) {
        mView.resetErrors();
        mView.setSavingIndicator(true);

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

        opportunity.setOngoing(mIsOngoing);
        if (!mIsOngoing) {
            if (mStartDateSet || mStartTimeSet)
                opportunity.setStartAt(DateHelper.format(DateHelper.iso8601Format, mStartAt.getTime()));
            opportunity.setOngoing(false);
            if (mEndDateSet || mEndTimeSet)
                opportunity.setEndAt(DateHelper.format(DateHelper.iso8601Format, mEndAt.getTime()));
            opportunity.setOngoing(false);

            opportunity.setStartDateSet(mStartDateSet);
            opportunity.setEndDateSet(mEndDateSet);
            opportunity.setStartTimeSet(mStartTimeSet);
            opportunity.setEndTimeSet(mEndTimeSet);
        }

        Callback<Opportunity> opportunityCallback = new Callback<Opportunity>() {
            @Override
            public void onResponse(Call<Opportunity> call, Response<Opportunity> response) {
                mView.setSavingIndicator(false);
                mView.close();
            }

            @Override
            public void onFailure(Call<Opportunity> call, Throwable t) {
                mView.setSavingIndicator(false);
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
                        if (index < 0) {
                            mContacts.add(contact);
                        } else {
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

    @Override
    public void onNewPlaceSelected(Place place) {
        mCurrentPlace = place;
        mView.setLocation(place.getName().toString());
    }

    @Override
    public void onLocationTypeChanged(OpportunityFragment.LocationType locationType) {
        mIsVirtual = locationType == OpportunityFragment.LocationType.VIRTUAL;
        mView.setShowLocationGroup(locationType == OpportunityFragment.LocationType.LOCATION);
    }

    @Override
    public void onTimeTypeChanged(OpportunityFragment.TimeType timeType) {
        mIsOngoing = timeType == OpportunityFragment.TimeType.ONGOING;
        mView.setShowTimeGroup(timeType == OpportunityFragment.TimeType.DATED);
    }

    @Override
    public void startDateClicked() {
        mView.showStartDatePicker(mStartAt.get(Calendar.YEAR), mStartAt.get(Calendar.MONTH), mStartAt.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void startTimeClicked() {
        mView.showStartTimePicker(mStartAt.get(Calendar.HOUR_OF_DAY), mStartAt.get(Calendar.MINUTE), true);
    }

    @Override
    public void endDateClicked() {
        mView.showEndDatePicker(mEndAt.get(Calendar.YEAR), mEndAt.get(Calendar.MONTH), mEndAt.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void endTimeClicked() {
        mView.showEndTimePicker(mEndAt.get(Calendar.HOUR_OF_DAY), mEndAt.get(Calendar.MINUTE), true);
    }

    @Override
    public void onStartDateSelected(int year, int month, int dayOfMonth) {
        mStartDateSet = true;
        mStartAt.set(Calendar.YEAR, year);
        mStartAt.set(Calendar.MONTH, month);
        mStartAt.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mView.setStartDate(DateHelper.format(DateHelper.dateFormat, year, month, dayOfMonth));
    }

    @Override
    public void onStartTimeSelected(int hourOfDay, int minute) {
        mStartTimeSet = true;
        mStartAt.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mStartAt.set(Calendar.MINUTE, minute);
        mView.setStartTime(DateHelper.format(DateHelper.timeFormat, mStartAt.get(Calendar.YEAR), mStartAt.get(Calendar.MONTH), mStartAt.get(Calendar.DAY_OF_MONTH), hourOfDay, minute));
    }

    @Override
    public void onEndDateSelected(int year, int month, int dayOfMonth) {
        mEndDateSet = true;
        mEndAt.set(Calendar.YEAR, year);
        mEndAt.set(Calendar.MONTH, month);
        mEndAt.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mView.setEndDate(DateHelper.format(DateHelper.dateFormat, year, month, dayOfMonth));
    }

    @Override
    public void onEndTimeSelected(int hourOfDay, int minute) {
        mEndTimeSet = true;
        mEndAt.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mEndAt.set(Calendar.MINUTE, minute);
        mView.setEndTime(DateHelper.format(DateHelper.timeFormat, mEndAt.get(Calendar.YEAR), mEndAt.get(Calendar.MONTH), mEndAt.get(Calendar.DAY_OF_MONTH), hourOfDay, minute));
    }

}
