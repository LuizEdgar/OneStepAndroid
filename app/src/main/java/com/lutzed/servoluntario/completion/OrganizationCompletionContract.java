package com.lutzed.servoluntario.completion;

import android.graphics.Bitmap;

import com.lutzed.servoluntario.interfaces.BasePresenter;
import com.lutzed.servoluntario.interfaces.BaseView;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public interface OrganizationCompletionContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void navigateToChooseSkills();

        void resetErrors();

        void navigateToChooseCauses();

        void navigateToMain();

        void setAboutField(String about);

        void setMissionField(String goal);

        void setSiteField(String site);

        void showDefaultSaveError();

        void showProfileImageTypePicker();

        void showAddNewImageFromGallery(int request);

        void showAddNewImageFromCamera(int request);

        void onRequestPermissionsResultFromPresenter(int requestCode, String[] permissions, int[] grantResults);

        void setProfileImage(Bitmap bitmap);

        void setProfileImage(String url);
    }

    interface Presenter extends BasePresenter {
        void saveProfile(String about, String mission, String site);

        void addNewProfileImage();

        void addNewImageFromCamera(int request);

        void addNewImageFromGallery(int request);

        void onNewProfileImageAdded(Bitmap proportionalResizedBitmap);
    }
}
