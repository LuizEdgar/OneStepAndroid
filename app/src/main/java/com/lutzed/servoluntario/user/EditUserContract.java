package com.lutzed.servoluntario.user;

import android.graphics.Bitmap;

import com.google.android.gms.location.places.Place;
import com.lutzed.servoluntario.interfaces.BasePresenter;
import com.lutzed.servoluntario.interfaces.BaseView;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.Image;
import com.lutzed.servoluntario.models.SelectableItem;
import com.lutzed.servoluntario.models.Volunteer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public interface EditUserContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void addUniqueCauses(List<? extends SelectableItem> causes, List<? extends SelectableItem> toRemove);

        void addUniqueSkills(List<? extends SelectableItem> skills, List<? extends SelectableItem> toRemove);

        void showCreateNewContact(Contact contact);

        void showAddNewCause(List<Long> checkedItems);

        void showAddNewSkill(List<Long> checkedItems);

        void setName(String name);

        void setAbout(String about);

        void setMission(String mission);

        void setSavingIndicator(boolean active);

        void close();

        void setLocation(String location);

        void showEstablishedAtPicker(int year, int month, int dayOfMonth);

        void showBirthAtPicker(int year, int month, int dayOfMonth);

        void showNameRequiredError();

        void setNameFocus();

        void showContactRequiredError();

        void setFocusContactField();

        void showSkillsMinimumRequiredError(int minSkillsRequired);

        void setFocusSkills();

        void showCausesMinimumRequiredError(int minCausesRequired);

        void setFocusCauses();

        void showLocationRequiredError();

        void setFocusLocation();

        void setCnpj(String cnpj);

        void setProfileImage(String url);

        void setEstablishedAt(String format);

        void setUsername(String username);

        void setEmail(String email);

        void setSite(String site);

        void setContact(String name);

        void setOccupation(String occupation);

        void setShowPasswordField(boolean visible);

        void setGender(Volunteer.GenderEnum genderEnum);

        void setSize(Integer size);

        void setBirthAt(String format);

        void triggerSaveVolunteer();

        void triggerSaveOrganization();

        void showUsernameRequiredError();

        void setFocusUsernameField();

        void showInvalidUsernameError();

        void showEmailRequiredError();

        void setFocusEmailField();

        void showInvalidEmailError();

        void addImages(List<Image> images);

        void showImageTypePicker();

        void showProfileImageTypePicker();

        void showAddNewImageFromGallery(int request);

        void showAddNewImageFromCamera(int request);

        void removeImageItem(Image imagem, int position);

        void onRequestPermissionsResultFromPresenter(int requestCode, String[] permissions, int[] grantResults);

        void setProfileImage(Bitmap bitmap);
    }

    interface Presenter extends BasePresenter {
        void attemptSaveUser();

        void createNewContact();

        void addNewCause(List<Long> existingCauseIds);

        void addNewSkill(List<Long> existingSkillIds);

        void onNewItemsSelection(ArrayList<SelectableItem> parcelableArrayListExtra, ArrayList<SelectableItem> parcelableArrayListExtra1);

        void attemptSaveVolunteer(String email, String username, String name, String about, List<Long> skillIds, List<Long> causeIds, Volunteer.GenderEnum genderEnum, String occupation);

        void addNewContact(String name, String phone, String email);

        void onNewPlaceSelected(Place place);

        void onEstablishedDateSelected(int year, int month, int dayOfMonth);

        void onBirthDateSelected(int year, int month, int dayOfMonth);

        void onNewImageAdded(Bitmap bitmap);

        void removeImage(Image image, int position);

        void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults);

        void birthAtClicked();

        void establishedAtClicked();

        void addNewImage();

        void addNewProfileImage();

        void addNewImageFromCamera(int request);

        void addNewImageFromGallery(int request);

        void onNewProfileImageAdded(Bitmap proportionalResizedBitmap);
    }
}
