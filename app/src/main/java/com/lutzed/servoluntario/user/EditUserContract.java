package com.lutzed.servoluntario.user;

import android.graphics.Bitmap;

import com.google.android.gms.location.places.Place;
import com.lutzed.servoluntario.interfaces.BasePresenter;
import com.lutzed.servoluntario.interfaces.BaseView;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.Image;
import com.lutzed.servoluntario.models.SelectableItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public interface EditUserContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void setContacts(List<Contact> contacts);

        void addUniqueCauses(List<? extends SelectableItem> causes, List<? extends SelectableItem> toRemove);

        void addUniqueSkills(List<? extends SelectableItem> skills, List<? extends SelectableItem> toRemove);

        void showCreateNewContact();

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

        void addImages(List<Image> images);

        void showImageTypePicker();

        void showAddNewImageFromGallery();

        void showAddNewImageFromCamera();

        void removeImageItem(Image imagem, int position);

        void onRequestPermissionsResultFromPresenter(int requestCode, String[] permissions, int[] grantResults);

        void setCnpj(String cnpj);

        void setProfileImage(String url);

        void setEstablishedAt(String format);
    }

    interface Presenter extends BasePresenter {
        void attemptSaveUser(String title, String description, Contact contact, List<Long> skillIds, List<Long> causeIds, String volunteersNumber, String timeCommitment, String othersRequirements, String tags);

        void createNewContact();

        void addNewCause(List<Long> existingCauseIds);

        void addNewSkill(List<Long> existingSkillIds);

        void onNewItemsSelection(ArrayList<SelectableItem> parcelableArrayListExtra, ArrayList<SelectableItem> parcelableArrayListExtra1);

        void addNewContact(String name, String phone, String email);

        void onNewPlaceSelected(Place place);

        void onEstablishedDateSelected(int year, int month, int dayOfMonth);

        void onBirthDateSelected(int year, int month, int dayOfMonth);

        void addNewImage();

        void addNewImageFromCamera();

        void addNewImageFromGallery();

        void onNewImageAdded(Bitmap bitmap);

        void removeImage(Image image, int position);

        void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults);

        void birthAtClicked();

        void establishedAtClicked();

    }
}
