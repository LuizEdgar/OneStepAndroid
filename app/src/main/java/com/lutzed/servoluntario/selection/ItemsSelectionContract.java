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

        void showDefaultSaveError();

        void showNoItems();

        void showLoadingError();

        void showItems(List<? extends SelectableItem> items, boolean isSwap);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loadItems(boolean isRefresh);

        void saveSelection();
    }
}
