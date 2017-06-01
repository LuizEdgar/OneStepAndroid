package com.lutzed.servoluntario.opportunities;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.google.android.gms.location.places.Place;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Cause;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.Image;
import com.lutzed.servoluntario.models.Location;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.models.SelectableItem;
import com.lutzed.servoluntario.models.Skill;
import com.lutzed.servoluntario.util.AuthHelper;
import com.lutzed.servoluntario.util.Constants;
import com.lutzed.servoluntario.util.DateHelper;
import com.lutzed.servoluntario.util.Snippets;

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

public class EditOpportunityPresenter implements EditOpportunityContract.Presenter {

    private final EditOpportunityContract.View mView;
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
    private List<Image> mLocalImages;
    private List<Image> mImagesToDestroy;

    public EditOpportunityPresenter(EditOpportunityFragment editOpportunityFragment, Api.ApiClient apiClient, AuthHelper authHelper, Opportunity opportunity) {
        mView = editOpportunityFragment;
        mApiClient = apiClient;
        mAuthHelper = authHelper;
        mView.setPresenter(this);
        mOpportunity = opportunity;
        mContacts = new ArrayList<>();
        mLocalImages = new ArrayList<>();
        mImagesToDestroy = new ArrayList<>();
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
            mView.addImages(mOpportunity.getImages());
            Contact contact = mOpportunity.getContact();
            if (contact != null) {
                ArrayList<Contact> contacts = new ArrayList<>();
                contacts.add(contact);
                mView.setContacts(contacts, contact.getId());
            }

            mIsVirtual = mOpportunity.getVirtual();
            if (!mOpportunity.getVirtual()) {
                mView.setLocationGroupType(Opportunity.LocationType.LOCATION);
                if (mOpportunity.getLocation() != null) {
                    mView.setLocation(mOpportunity.getLocation().getName());
                }
            } else {
                mView.setLocationGroupType(Opportunity.LocationType.VIRTUAL);
            }

            mIsOngoing = mOpportunity.getOngoing();
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
            if (!mOpportunity.getOngoing()) {
                mView.setTimeGroupType(Opportunity.TimeType.DATED);
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
                mView.setTimeGroupType(Opportunity.TimeType.ONGOING);
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

        boolean cancel = false;

        boolean isUpdate = mOpportunity != null;

        // Check for a valid name
        if (TextUtils.isEmpty(title)) {
            mView.showTitleRequiredError();
            mView.setTitleFocus();
            cancel = true;
        }

        if (contact == null) {
            mView.showContactRequiredError();
            if (!cancel) mView.setFocusContactField();
            cancel = true;
        }

        int minCausesRequired = Constants.MIN_OPPORTUNITY_CAUSES_REQUIRED;
        if (minCausesRequired > 0 && causeIds.isEmpty()) {
            mView.showCausesMinimumRequiredError(minCausesRequired);
            if (!cancel) mView.setFocusCauses();
            cancel = true;
        }

        int minSkillsRequired = Constants.MIN_OPPORTUNITY_SKILLS_REQUIRED;
        if (minSkillsRequired > 0 && skillIds.isEmpty()) {
            mView.showSkillsMinimumRequiredError(minSkillsRequired);
            if (!cancel) mView.setFocusSkills();
            cancel = true;
        }

        if (!mIsVirtual && mCurrentPlace == null && isUpdate && mOpportunity.getLocation() == null) {
            mView.showLocationRequiredError();
            if (!cancel) mView.setFocusLocation();
            cancel = true;
        }

        if (!mIsOngoing) {
            if (!mStartDateSet) {
                mView.showStartDateRequiredError();
                if (!cancel) mView.setFocusTime();
                cancel = true;
            }

            if (!mEndDateSet) {
                mView.showEndDateRequiredError();
                if (!cancel) mView.setFocusTime();
                cancel = true;
            }

            if (mStartDateSet && mEndDateSet && mEndAt.before(mStartAt)) {
                mView.showEndBeforeStartError();
                if (!cancel) mView.setFocusTime();
                cancel = true;
            }
        }

        if (!cancel) {
            mView.setSavingIndicator(true);

            int volunteersNumberInt = 10;

            Opportunity opportunity = new Opportunity();
            if (isUpdate) {
                opportunity.setId(mOpportunity.getId());
                opportunity.setImagesAttributes(mImagesToDestroy);
            }
            opportunity.setTitle(title);
            opportunity.setDescription(description);
            if (isUpdate && mOpportunity.getContact() != null)
                contact.setId(mOpportunity.getContact().getId());
            opportunity.setContactAttributes(contact);
            opportunity.setVolunteersNumber(volunteersNumberInt);
            opportunity.setTimeCommitment(timeCommitment);
            opportunity.setOthersRequirements(othersRequirements);
            opportunity.setCauseIds(causeIds);
            opportunity.setSkillIds(skillIds);
            opportunity.setTags(tags);

            opportunity.setVirtual(mIsVirtual);
            if (!mIsVirtual && mCurrentPlace != null) {
                Location location = new Location();
                if (isUpdate && mOpportunity.getLocation() != null)
                    location.setId(mOpportunity.getLocation().getId());
                location.setName(mCurrentPlace.getName().toString());
                location.setAddress1(mCurrentPlace.getAddress().toString());
                location.setGooglePlacesId(mCurrentPlace.getId());
                location.setLatitude(mCurrentPlace.getLatLng().latitude);
                location.setLongitude(mCurrentPlace.getLatLng().longitude);
                opportunity.setLocationAttributes(location);
            }

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

            if (!mLocalImages.isEmpty()) {
                List<String> base64Images = new ArrayList<>();
                for (Image mLocalImage : mLocalImages) {
                    base64Images.add(Snippets.encodeToBase64(mLocalImage.getBitmap(), true));
                }
                opportunity.setImagesAttributes64(base64Images);
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
    public void addNewImage() {
        mView.showImageTypePicker();
    }

    @Override
    public void addNewImageFromCamera() {
        mView.showAddNewImageFromCamera();
    }

    @Override
    public void addNewImageFromGallery() {
        mView.showAddNewImageFromGallery();
    }

    @Override
    public void onNewImageAdded(Bitmap bitmap) {
        Image image = new Image(bitmap);
        image.setId(new Random().nextLong());
        mLocalImages.add(image);
        ArrayList<Image> images = new ArrayList<>();
        images.add(image);
        mView.addImages(images);
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
    public void onLocationTypeChanged(Opportunity.LocationType locationType) {
        mIsVirtual = locationType == Opportunity.LocationType.VIRTUAL;
        mView.setShowLocationGroup(locationType == Opportunity.LocationType.LOCATION);
    }

    @Override
    public void onTimeTypeChanged(Opportunity.TimeType timeType) {
        mIsOngoing = timeType == Opportunity.TimeType.ONGOING;
        mView.setShowTimeGroup(timeType == Opportunity.TimeType.DATED);
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

    @Override
    public void removeImage(Image image, int position) {
        boolean removed = mLocalImages.remove(image);
        if (!removed) {
            image.setDestroy(true);
            mImagesToDestroy.add(image);
        }
        mView.removeImageItem(image, position);
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        mView.onRequestPermissionsResultFromPresenter(requestCode, permissions, grantResults);
    }
}
