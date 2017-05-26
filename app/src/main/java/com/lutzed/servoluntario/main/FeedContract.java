package com.lutzed.servoluntario.main;

import com.lutzed.servoluntario.interfaces.BasePresenter;
import com.lutzed.servoluntario.interfaces.BaseView;
import com.lutzed.servoluntario.models.FeedItem;

import java.util.List;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public interface FeedContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void clearItems();

        void swapItems(List<FeedItem> items);

        void addItems(List<FeedItem> items);

        void showLoadingError();

        void showNoItems();
    }

    interface Presenter extends BasePresenter {
        void loadItems(boolean isRefresh);

        void refreshItems();
    }
}
