package com.lutzed.servoluntario.opportunities;

import android.graphics.Bitmap;

import com.google.android.gms.location.places.Place;
import com.lutzed.servoluntario.interfaces.BasePresenter;
import com.lutzed.servoluntario.interfaces.BaseView;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.Image;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.models.SelectableItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public interface EditOpportunityContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void resetErrors();

        void setContacts(List<Contact> contacts, Long selectedContactId);

        void addUniqueCauses(List<? extends SelectableItem> causes, List<? extends SelectableItem> toRemove);

        void addUniqueSkills(List<? extends SelectableItem> skills, List<? extends SelectableItem> toRemove);

        void showCreateNewContact();

        void showAddNewCause(List<Long> checkedItems);

        void showAddNewSkill(List<Long> checkedItems);

        void setTitle(String title);

        void setDescription(String description);

        void setVolunteersNumber(Integer volunteersNumber);

        void setTimeCommitment(String timeCommitment);

        void setOthersRequirements(String othersRequirements);

        void setTags(String tags);

        void setSavingIndicator(boolean active);

        void close();

        void setLocation(String location);

        void showStartDatePicker(int year, int month, int dayOfMonth);

        void showEndDatePicker(int year, int month, int dayOfMonth);

        void showStartTimePicker(int hourOfDay, int minute, boolean is24HourView);

        void showEndTimePicker(int hourOfDay, int minute, boolean is24HourView);

        void setShowTimeGroup(boolean visible);

        void setShowLocationGroup(boolean visible);

        void setStartDate(String startDate);

        void setEndDate(String endDate);

        void setStartTime(String startTime);

        void setEndTime(String endTime);

        void setTimeGroupType(Opportunity.TimeType timeType);

        void setLocationGroupType(Opportunity.LocationType locationType);

        void showTitleRequiredError();

        void setTitleFocus();

        void showContactRequiredError();

        void setFocusContactField();

        void showSkillsMinimumRequiredError(int minSkillsRequired);

        void setFocusSkills();

        void showCausesMinimumRequiredError(int minCausesRequired);

        void setFocusCauses();

        void showLocationRequiredError();

        void setFocusLocation();

        void setFocusTime();

        void showEndBeforeStartError();

        void showStartDateRequiredError();

        void showEndDateRequiredError();

        void addImages(List<Image> images);

        void showImageTypePicker();

        void showAddNewImageFromGallery();

        void showAddNewImageFromCamera();

        void removeImageItem(Image imagem, int position);

        void onRequestPermissionsResultFromPresenter(int requestCode, String[] permissions, int[] grantResults);
    }

    interface Presenter extends BasePresenter {
        void attemptCreateOpportunity(String title, String description, Contact contact, List<Long> skillIds, List<Long> causeIds, String volunteersNumber, String timeCommitment, String othersRequirements, String tags);

        void loadContacts();

        void createNewContact();

        void addNewCause(List<Long> existingCauseIds);

        void addNewSkill(List<Long> existingSkillIds);

        void onNewItemsSelection(ArrayList<SelectableItem> parcelableArrayListExtra, ArrayList<SelectableItem> parcelableArrayListExtra1);

        void addNewContact(String name, String phone, String email);

        void onNewPlaceSelected(Place place);

        void onLocationTypeChanged(Opportunity.LocationType locationType);

        void onTimeTypeChanged(Opportunity.TimeType timeType);

        void startDateClicked();

        void endDateClicked();

        void startTimeClicked();

        void endTimeClicked();

        void onStartDateSelected(int year, int month, int dayOfMonth);

        void onEndDateSelected(int year, int month, int dayOfMonth);

        void onStartTimeSelected(int hourOfDay, int minute);

        void onEndTimeSelected(int hourOfDay, int minute);

        void addNewImage();

        void addNewImageFromCamera();

        void addNewImageFromGallery();

        void onNewImageAdded(Bitmap bitmap);

        void removeImage(Image image, int position);

        void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults);
    }
}
