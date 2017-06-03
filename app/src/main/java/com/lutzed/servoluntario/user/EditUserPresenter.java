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

public class EditUserPresenter implements EditUserContract.Presenter {

    private final EditUserContract.View mView;
    private final Api.ApiClient mApiClient;
    private final AuthHelper mAuthHelper;
    private final User mUser;
    private Calendar mEstablishedAt;
    private Calendar mBirthAt;
    private boolean mEstablishedAtSet;
    private boolean mBirthAtSet;
    private Place mCurrentPlace;
    private List<Image> mLocalImages;
    private List<Image> mImagesToDestroy;
    private Contact mContact;
    private Image mProfileImage;

    public EditUserPresenter(EditUserFragment editUserFragment, Api.ApiClient apiClient, AuthHelper authHelper) {
        mView = editUserFragment;
        mApiClient = apiClient;
        mAuthHelper = authHelper;
        mUser = authHelper.getUser();
        mView.setPresenter(this);
        mLocalImages = new ArrayList<>();
        mImagesToDestroy = new ArrayList<>();
    }

    @Override
    public void start() {

        mEstablishedAt = Calendar.getInstance();
        mBirthAt = Calendar.getInstance();

        setupUserData();
        if (mUser.getKindEnum() == User.Kind.VOLUNTEER) {
            setupVolunteerData(mUser.getVolunteer());
        } else {
            setupOrganizationData(mUser.getOrganization());
        }
    }

    private void setupUserData() {
        mView.setEmail(mUser.getEmail());
        mView.setUsername(mUser.getUsername());
        mView.setShowPasswordField(true);
    }

    private void setupOrganizationData(Organization organization) {
        mView.setName(organization.getName());
        mView.setAbout(organization.getAbout());

        if (organization.getProfileImage() != null) {
            mProfileImage = organization.getProfileImage();
            mView.setProfileImage(mProfileImage.getUrl());
        }

        mView.setMission(organization.getMission());
        mView.setCnpj(organization.getCnpj());
        mView.setSite(organization.getSite());
        mView.setSize(organization.getSize());

        List<Contact> contacts = organization.getContacts();
        if (contacts != null && !contacts.isEmpty()) {
            mContact = contacts.get(0);
            mView.setContact(mContact.toString());
        }

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

    @Override
    public void attemptSaveOrganization(String email, String username, String name, String about, List<Long> skillIds, List<Long> causeIds, String size, String cnpj, String mission, String site) {
        boolean cancel = false;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(username)) {
            mView.showUsernameRequiredError();
            if (!cancel) mView.setFocusUsernameField();
            cancel = true;
        } else if (!Snippets.isUsernameValid(username)) {
            mView.showInvalidUsernameError();
            if (!cancel) mView.setFocusUsernameField();
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mView.showEmailRequiredError();
            if (!cancel) mView.setFocusEmailField();
            cancel = true;
        } else if (!Snippets.isEmailValid(email)) {
            mView.showInvalidEmailError();
            if (!cancel) mView.setFocusEmailField();
            cancel = true;
        }

        // Check for a valid name
        if (TextUtils.isEmpty(name)) {
            mView.showNameRequiredError();
            mView.setNameFocus();
            cancel = true;
        }

        if (mContact == null && (mUser.getOrganization().getContacts() == null || mUser.getOrganization().getContacts().isEmpty())) {
            mView.showContactRequiredError();
            if (!cancel) mView.setFocusContactField();
            cancel = true;
        }

        if (mCurrentPlace == null && (mUser.getOrganization().getLocations() == null || mUser.getOrganization().getLocations().isEmpty())) {
            mView.showLocationRequiredError();
            if (!cancel) mView.setFocusLocation();
            cancel = true;
        }

        int minCausesRequired = Constants.MIN_ORGANIZATIONS_CAUSES_REQUIRED;
        if (minCausesRequired > 0 && causeIds.isEmpty()) {
            mView.showCausesMinimumRequiredError(minCausesRequired);
            if (!cancel) mView.setFocusCauses();
            cancel = true;
        }

        int minSkillsRequired = Constants.MIN_ORGANIZATIONS_SKILLS_REQUIRED;
        if (minSkillsRequired > 0 && skillIds.isEmpty()) {
            mView.showSkillsMinimumRequiredError(minSkillsRequired);
            if (!cancel) mView.setFocusSkills();
            cancel = true;
        }

        if (!cancel) {
            mView.setSavingIndicator(true);

            User user = new User();
            user.setId(mUser.getId());

            user.setEmail(email);
            user.setUsername(username);

            Organization organization = new Organization();
            organization.setId(mUser.getOrganization().getId());

            organization.setName(name);
            organization.setAbout(about);

            Integer sizeInt;
            try {
                sizeInt = Integer.parseInt(size);
            } catch (NumberFormatException e) {
                sizeInt = null;
            }
            organization.setSize(sizeInt);
            organization.setCnpj(cnpj);
            organization.setSite(site);
            organization.setMission(mission);

            List<Contact> contacts = mUser.getOrganization().getContacts();
            if (contacts != null && !contacts.isEmpty()) {
                mContact.setId(contacts.get(0).getId());
            }
            List<Contact> contactsAttributes = new ArrayList<>();
            contactsAttributes.add(mContact);
            organization.setContactsAttributes(contactsAttributes);

            if (mCurrentPlace != null) {
                Location location = new Location();
                List<Location> locations = mUser.getOrganization().getLocations();
                if (locations != null && !locations.isEmpty()) {
                    location.setId(locations.get(0).getId());
                }
                location.setName(mCurrentPlace.getName().toString());
                location.setAddress1(mCurrentPlace.getAddress().toString());
                location.setGooglePlacesId(mCurrentPlace.getId());
                location.setLatitude(mCurrentPlace.getLatLng().latitude);
                location.setLongitude(mCurrentPlace.getLatLng().longitude);

                List<Location> locationsAttributes = new ArrayList<>();
                locationsAttributes.add(location);
                organization.setLocationsAttributes(locationsAttributes);
            }

            organization.setCauseIds(causeIds);
            organization.setSkillIds(skillIds);
            if (mProfileImage != null && mProfileImage.getBitmap() != null) {
                organization.setProfileImage64(Snippets.encodeToBase64(mProfileImage.getBitmap(), true));
            }

            if (mEstablishedAtSet)
                organization.setEstablishedAt(DateHelper.format(DateHelper.iso8601Format, mEstablishedAt.getTime()));

            if (!mLocalImages.isEmpty()) {
                List<String> base64Images = new ArrayList<>();
                for (Image mLocalImage : mLocalImages) {
                    base64Images.add(Snippets.encodeToBase64(mLocalImage.getBitmap(), true));
                }
                organization.setImagesAttributes64(base64Images);
            }
            organization.setImagesAttributes(mImagesToDestroy);

            user.setOrganizationAttributes(organization);

            Callback<User> updateCallback = new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    mAuthHelper.setUser(response.body());
                    mView.setSavingIndicator(false);
                    mView.close();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    mView.setSavingIndicator(false);
                    t.printStackTrace();
                }
            };

            mApiClient.updateUser(mUser.getId(), user).enqueue(updateCallback);
        }
    }

    private void setupVolunteerData(Volunteer volunteer) {
        mView.setName(volunteer.getName());
        mView.setAbout(volunteer.getAbout());

        if (volunteer.getProfileImage() != null) {
            mProfileImage = volunteer.getProfileImage();
            mView.setProfileImage(mProfileImage.getUrl());
        }

        mView.setOccupation(volunteer.getOccupation());
        mView.setGender(volunteer.getGenderEnum());

        List<Contact> contacts = volunteer.getContacts();
        if (contacts != null && !contacts.isEmpty()) {
            mContact = contacts.get(0);
            mView.setContact(mContact.toString());
        }

        List<Location> locations = volunteer.getLocations();
        if (locations != null && !locations.isEmpty()) {
            mView.setLocation(locations.get(0).getName());
        }

        String birthAt = volunteer.getBirthAt();
        if (!TextUtils.isEmpty(birthAt)) {
            mBirthAt = DateHelper.deserializeToCalendar(birthAt);
            mView.setBirthAt(DateHelper.format(DateHelper.yearFormat, birthAt));
        } else {
            mBirthAt = Calendar.getInstance();
        }

        mView.addUniqueCauses(volunteer.getCauses(), null);
        mView.addUniqueSkills(volunteer.getSkills(), null);
    }

    @Override
    public void attemptSaveVolunteer(String email, String username, String name, String about, List<Long> skillIds, List<Long> causeIds, Volunteer.GenderEnum genderEnum, String occupation) {
        boolean cancel = false;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(username)) {
            mView.showUsernameRequiredError();
            if (!cancel) mView.setFocusUsernameField();
            cancel = true;
        } else if (!Snippets.isUsernameValid(username)) {
            mView.showInvalidUsernameError();
            if (!cancel) mView.setFocusUsernameField();
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mView.showEmailRequiredError();
            if (!cancel) mView.setFocusEmailField();
            cancel = true;
        } else if (!Snippets.isEmailValid(email)) {
            mView.showInvalidEmailError();
            if (!cancel) mView.setFocusEmailField();
            cancel = true;
        }

        // Check for a valid name
        if (TextUtils.isEmpty(name)) {
            mView.showNameRequiredError();
            mView.setNameFocus();
            cancel = true;
        }

        if (mContact == null && (mUser.getVolunteer().getContacts() == null || mUser.getVolunteer().getContacts().isEmpty())) {
            mView.showContactRequiredError();
            if (!cancel) mView.setFocusContactField();
            cancel = true;
        }

        int minCausesRequired = Constants.MIN_VOLUNTEERS_CAUSES_REQUIRED;
        if (minCausesRequired > 0 && causeIds.isEmpty()) {
            mView.showCausesMinimumRequiredError(minCausesRequired);
            if (!cancel) mView.setFocusCauses();
            cancel = true;
        }

        int minSkillsRequired = Constants.MIN_VOLUNTEERS_SKILLS_REQUIRED;
        if (minSkillsRequired > 0 && skillIds.isEmpty()) {
            mView.showSkillsMinimumRequiredError(minSkillsRequired);
            if (!cancel) mView.setFocusSkills();
            cancel = true;
        }

        if (!cancel) {
            mView.setSavingIndicator(true);

            User user = new User();
            user.setId(mUser.getId());

            user.setEmail(email);
            user.setUsername(username);

            Volunteer volunteer = new Volunteer();
            volunteer.setId(mUser.getVolunteer().getId());

            volunteer.setName(name);
            volunteer.setAbout(about);

            List<Contact> contacts = mUser.getVolunteer().getContacts();
            if (contacts != null && !contacts.isEmpty()) {
                mContact.setId(contacts.get(0).getId());
            }
            List<Contact> contactsAttributes = new ArrayList<>();
            contactsAttributes.add(mContact);
            volunteer.setContactsAttributes(contactsAttributes);

            if (mCurrentPlace != null) {
                Location location = new Location();
                List<Location> locations = mUser.getVolunteer().getLocations();
                if (locations != null && !locations.isEmpty()) {
                    location.setId(locations.get(0).getId());
                }
                location.setName(mCurrentPlace.getName().toString());
                location.setAddress1(mCurrentPlace.getAddress().toString());
                location.setGooglePlacesId(mCurrentPlace.getId());
                location.setLatitude(mCurrentPlace.getLatLng().latitude);
                location.setLongitude(mCurrentPlace.getLatLng().longitude);

                List<Location> locationsAttributes = new ArrayList<>();
                locationsAttributes.add(location);
                volunteer.setLocationsAttributes(locationsAttributes);
            }

            volunteer.setGender(genderEnum.getValue());
            volunteer.setOccupation(occupation);
            volunteer.setCauseIds(causeIds);
            volunteer.setSkillIds(skillIds);
            if (mProfileImage != null && mProfileImage.getBitmap() != null) {
                volunteer.setProfileImage64(Snippets.encodeToBase64(mProfileImage.getBitmap(), true));
            }

            if (mBirthAtSet)
                volunteer.setBirthAt(DateHelper.format(DateHelper.iso8601Format, mBirthAt.getTime()));

            user.setVolunteerAttributes(volunteer);

            Callback<User> updateCallback = new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    mAuthHelper.setUser(response.body());
                    mView.setSavingIndicator(false);
                    mView.close();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    mView.setSavingIndicator(false);
                    t.printStackTrace();
                }
            };

            mApiClient.updateUser(mUser.getId(), user).enqueue(updateCallback);
        }
    }

    @Override
    public void addNewContact(String name, String phone, String email) {
        mContact = new Contact(name, phone, email);
        mView.setContact(mContact.toString());
    }

    @Override
    public void attemptSaveUser() {
        if (mUser.getKindEnum() == User.Kind.VOLUNTEER) {
            mView.triggerSaveVolunteer();
        } else {
            mView.triggerSaveOrganization();
        }
    }

    @Override
    public void createNewContact() {
        mView.showCreateNewContact(mContact);
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
    public void onNewItemsSelection
            (ArrayList<SelectableItem> selectedItems, ArrayList<SelectableItem> notSelectedItems) {
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
        mEstablishedAtSet = true;
        mEstablishedAt.set(Calendar.YEAR, year);
        mEstablishedAt.set(Calendar.MONTH, month);
        mEstablishedAt.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mView.setEstablishedAt(DateHelper.format(DateHelper.yearFormat, year, month, dayOfMonth));
    }

    @Override
    public void birthAtClicked() {
        mView.showEstablishedAtPicker(mBirthAt.get(Calendar.YEAR), mBirthAt.get(Calendar.MONTH), mBirthAt.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onBirthDateSelected(int year, int month, int dayOfMonth) {
        mBirthAtSet = true;
        mEstablishedAt.set(Calendar.YEAR, year);
        mEstablishedAt.set(Calendar.MONTH, month);
        mEstablishedAt.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mView.setEstablishedAt(DateHelper.format(DateHelper.yearFormat, year, month, dayOfMonth));
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
    public void onRequestPermissionResult(int requestCode, String[] permissions,
                                          int[] grantResults) {
        mView.onRequestPermissionsResultFromPresenter(requestCode, permissions, grantResults);
    }
}
