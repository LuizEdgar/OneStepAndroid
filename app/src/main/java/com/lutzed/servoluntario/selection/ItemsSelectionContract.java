package com.lutzed.servoluntario.selection;

import com.lutzed.servoluntario.interfaces.BasePresenter;
import com.lutzed.servoluntario.interfaces.BaseView;
import com.lutzed.servoluntario.models.SelectableItem;

import java.util.List;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public interface ItemsSelectionContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void navigateToChooseSkills();

        void navigateToChooseCauses();

        void navigateToMain();

        void showDefaultSaveError();

        void showNoItems();

        void showLoadingError();

        void clearItems();

        void swapItems(List<? extends SelectableItem> items);

        void addItems(List<? extends SelectableItem> items);

        boolean isActive();

        void setSavingIndicator(boolean active);
    }

    interface Presenter extends BasePresenter {
        void loadItems(boolean isRefresh);

        void saveItemsToUser(List<Long> selectedIds);

        void refreshItems();
    }
}
