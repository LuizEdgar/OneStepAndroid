package com.lutzed.servoluntario.user;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.google.android.gms.location.places.Place;
import com.lutzed.servoluntario.api.Api;
import com.lutzed.servoluntario.models.Cause;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.Image;
import com.lutzed.servoluntario.models.Location;
import com.lutzed.servoluntario.models.Organization;
import com.lutzed.servoluntario.models.SelectableItem;
import com.lutzed.servoluntario.models.Skill;
import com.lutzed.servoluntario.models.User;
import com.lutzed.servoluntario.models.Volunteer;
import com.lutzed.servoluntario.util.AuthHelper;
import com.lutzed.servoluntario.util.DateHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public class EditUserPresenter implements EditUserContract.Presenter {

    private final EditUserContract.View mView;
    private final Api.ApiClient mApiClient;
    private final AuthHelper mAuthHelper;
    private final User mUser;
    private Calendar mEstablishedAt;
    private Calendar mBirthAt;
    private boolean mMultiDateSet;
    private Place mCurrentPlace;
    private List<Contact> mContacts;
    private List<Image> mLocalImages;
    private List<Image> mImagesToDestroy;

    public EditUserPresenter(EditUserFragment editUserFragment, Api.ApiClient apiClient, AuthHelper authHelper) {
        mView = editUserFragment;
        mApiClient = apiClient;
        mAuthHelper = authHelper;
        mUser = authHelper.getUser();
        mView.setPresenter(this);
        mContacts = new ArrayList<>();
        mLocalImages = new ArrayList<>();
        mImagesToDestroy = new ArrayList<>();
    }

    @Override
    public void start() {

        if (mUser.getKindEnum() == User.Kind.VOLUNTEER) {
//            setupVolunteerData(mUser.getVolunteer());
        } else {
//            setupOrganizationData(mUser.getOrganization());
        }
    }

    private void setupOrganizationData(Organization organization) {
        if (organization.getProfileImage() != null)
            mView.setProfileImage(organization.getProfileImage().getUrl());

        mView.setName(organization.getName());
        mView.setAbout(organization.getAbout());
        mView.setMission(organization.getMission());
        mView.setCnpj(organization.getCnpj());
        mView.setContacts(organization.getContacts());

        List<Location> locations = organization.getLocations();
        if (locations != null && !locations.isEmpty()) {
            mView.setLocation(locations.get(0).getName());
        }

        String establishedAt = organization.getEstablishedAt();
        if (!TextUtils.isEmpty(establishedAt)) {
            mEstablishedAt = DateHelper.deserializeToCalendar(establishedAt);
            mView.setEstablishedAt(DateHelper.format(DateHelper.yearFormat, establishedAt));
        } else {
            mEstablishedAt = Calendar.getInstance();
        }

        mView.addUniqueCauses(organization.getCauses(), null);
        mView.addUniqueSkills(organization.getSkills(), null);
        mView.addImages(organization.getImages());
    }

    private void setupVolunteerData(Volunteer volunteer) {

    }

    @Override
    public void attemptSaveUser(String title, String description, Contact contact, List<Long> skillIds, List<Long> causeIds, String volunteersNumber, String timeCommitment, String othersRequirements, String tags) {
//        mView.resetErrors();
//
//        boolean cancel = false;
//
//        boolean isUpdate = mOpportunity != null;
//
//        // Check for a valid name
//        if (TextUtils.isEmpty(title)) {
//            mView.showNameRequiredError();
//            mView.setNameFocus();
//            cancel = true;
//        }
//
//        if (contact == null) {
//            mView.showContactRequiredError();
//            if (!cancel) mView.setFocusContactField();
//            cancel = true;
//        }
//
//        int minCausesRequired = Constants.MIN_OPPORTUNITY_CAUSES_REQUIRED;
//        if (minCausesRequired > 0 && causeIds.isEmpty()) {
//            mView.showCausesMinimumRequiredError(minCausesRequired);
//            if (!cancel) mView.setFocusCauses();
//            cancel = true;
//        }
//
//        int minSkillsRequired = Constants.MIN_OPPORTUNITY_SKILLS_REQUIRED;
//        if (minSkillsRequired > 0 && skillIds.isEmpty()) {
//            mView.showSkillsMinimumRequiredError(minSkillsRequired);
//            if (!cancel) mView.setFocusSkills();
//            cancel = true;
//        }
//
//        if (!mIsVirtual && mCurrentPlace == null && isUpdate && mOpportunity.getLocation() == null) {
//            mView.showLocationRequiredError();
//            if (!cancel) mView.setFocusLocation();
//            cancel = true;
//        }
//
//        if (!mIsOngoing) {
//            if (!mMultiDateSet) {
//                mView.showStartDateRequiredError();
//                if (!cancel) mView.setFocusTime();
//                cancel = true;
//            }
//
//            if (!mEndDateSet) {
//                mView.showEndDateRequiredError();
//                if (!cancel) mView.setFocusTime();
//                cancel = true;
//            }
//
//            if (mMultiDateSet && mEndDateSet && mEndAt.before(mEstablishedAt)) {
//                mView.showEndBeforeStartError();
//                if (!cancel) mView.setFocusTime();
//                cancel = true;
//            }
//        }
//
//        if (!cancel) {
//            mView.setSavingIndicator(true);
//
//            int volunteersNumberInt = 10;
//
//            Opportunity opportunity = new Opportunity();
//            if (isUpdate) {
//                opportunity.setId(mOpportunity.getId());
//                opportunity.setImagesAttributes(mImagesToDestroy);
//            }
//            opportunity.setName(title);
//            opportunity.setAbout(description);
//            if (isUpdate && mOpportunity.getContact() != null)
//                contact.setId(mOpportunity.getContact().getId());
//            opportunity.setContactAttributes(contact);
//            opportunity.setVolunteersNumber(volunteersNumberInt);
//            opportunity.setMission(timeCommitment);
//            opportunity.setOthersRequirements(othersRequirements);
//            opportunity.setCauseIds(causeIds);
//            opportunity.setSkillIds(skillIds);
//            opportunity.setTags(tags);
//
//            opportunity.setVirtual(mIsVirtual);
//            if (!mIsVirtual && mCurrentPlace != null) {
//                Location location = new Location();
//                if (isUpdate && mOpportunity.getLocation() != null)
//                    location.setId(mOpportunity.getLocation().getId());
//                location.setName(mCurrentPlace.getName().toString());
//                location.setAddress1(mCurrentPlace.getAddress().toString());
//                location.setGooglePlacesId(mCurrentPlace.getId());
//                location.setLatitude(mCurrentPlace.getLatLng().latitude);
//                location.setLongitude(mCurrentPlace.getLatLng().longitude);
//                opportunity.setLocationAttributes(location);
//            }
//
//            opportunity.setOngoing(mIsOngoing);
//            if (!mIsOngoing) {
//                if (mMultiDateSet || mStartTimeSet)
//                    opportunity.setStartAt(DateHelper.format(DateHelper.iso8601Format, mEstablishedAt.getTime()));
//                opportunity.setOngoing(false);
//                if (mEndDateSet || mEndTimeSet)
//                    opportunity.setEndAt(DateHelper.format(DateHelper.iso8601Format, mEndAt.getTime()));
//                opportunity.setOngoing(false);
//
//                opportunity.setStartDateSet(mMultiDateSet);
//                opportunity.setEndDateSet(mEndDateSet);
//                opportunity.setStartTimeSet(mStartTimeSet);
//                opportunity.setEndTimeSet(mEndTimeSet);
//            }
//
//            if (!mLocalImages.isEmpty()) {
//                List<String> base64Images = new ArrayList<>();
//                for (Image mLocalImage : mLocalImages) {
//                    base64Images.add(Snippets.encodeToBase64(mLocalImage.getBitmap(), true));
//                }
//                opportunity.setImagesAttributes64(base64Images);
//            }
//
//            Callback<Opportunity> opportunityCallback = new Callback<Opportunity>() {
//                @Override
//                public void onResponse(Call<Opportunity> call, Response<Opportunity> response) {
//                    mView.setSavingIndicator(false);
//                    mView.close();
//                }
//
//                @Override
//                public void onFailure(Call<Opportunity> call, Throwable t) {
//                    mView.setSavingIndicator(false);
//                    t.printStackTrace();
//                }
//            };
//
//            if (isUpdate)
//                mApiClient.updateOpportunity(opportunity.getId(), opportunity).enqueue(opportunityCallback);
//            else mApiClient.createOpportunity(opportunity).enqueue(opportunityCallback);
//        }
    }

    @Override
    public void addNewContact(String name, String phone, String email) {
        Contact contact = new Contact(name, phone, email);
        contact.setId(new Random().nextLong());
        mContacts.add(contact);
        mView.setContacts(mContacts);
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
    public void establishedAtClicked() {
        mView.showEstablishedAtPicker(mEstablishedAt.get(Calendar.YEAR), mEstablishedAt.get(Calendar.MONTH), mEstablishedAt.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEstablishedDateSelected(int year, int month, int dayOfMonth) {
        mMultiDateSet = true;
        mEstablishedAt.set(Calendar.YEAR, year);
        mEstablishedAt.set(Calendar.MONTH, month);
        mEstablishedAt.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mView.setEstablishedAt(DateHelper.format(DateHelper.dateFormat, year, month, dayOfMonth));
    }

    @Override
    public void birthAtClicked() {
        mView.showEstablishedAtPicker(mEstablishedAt.get(Calendar.YEAR), mEstablishedAt.get(Calendar.MONTH), mEstablishedAt.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onBirthDateSelected(int year, int month, int dayOfMonth) {
        mMultiDateSet = true;
        mEstablishedAt.set(Calendar.YEAR, year);
        mEstablishedAt.set(Calendar.MONTH, month);
        mEstablishedAt.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mView.setEstablishedAt(DateHelper.format(DateHelper.dateFormat, year, month, dayOfMonth));
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
