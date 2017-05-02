package com.lutzed.servoluntario.completion;

import com.lutzed.servoluntario.interfaces.BasePresenter;
import com.lutzed.servoluntario.interfaces.BaseView;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public interface VolunteerCompletionContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void navigateToChooseSkills();

        void resetErrors();

        void navigateToMain();

        void navigateToChooseCauses();

        void setAboutField(String about);

        void setOccupationField(String occupation);

        void showDefaultSaveError();
    }

    interface Presenter extends BasePresenter {
        void saveProfile(String about, String occupation);
    }
}
