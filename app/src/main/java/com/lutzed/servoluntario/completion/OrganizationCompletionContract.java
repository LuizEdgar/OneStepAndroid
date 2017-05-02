package com.lutzed.servoluntario.completion;

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

        void setNeedField(String need);

        void setGoalField(String goal);

        void setSiteField(String site);

        void showDefaultSaveError();
    }

    interface Presenter extends BasePresenter {
        void saveProfile(String about, String need, String goal, String site);
    }
}
