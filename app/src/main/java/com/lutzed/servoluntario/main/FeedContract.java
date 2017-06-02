package com.lutzed.servoluntario.main;

import com.lutzed.servoluntario.interfaces.BasePresenter;
import com.lutzed.servoluntario.interfaces.BaseView;
import com.lutzed.servoluntario.models.FeedItem;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.models.Organization;
import com.lutzed.servoluntario.models.Volunteer;

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

        void showOpportunity(Opportunity opportunity);

        void showOrganization(Organization organization);

        void showVolunteer(Volunteer volunteer);

        void showOrganization(Long organizationId);

        void showVolunteer(Long volunteerId);
    }

    interface Presenter extends BasePresenter {
        void loadItems(boolean isRefresh);

        void refreshItems();

        void opportunityClicked(Opportunity opportunity);

        void organizationClicked(Organization organization);

        void volunteerClicked(Volunteer volunteer);

        void organizationClicked(Long organizationId);

        void volunteerClicked(Long volunteerId);

        boolean hasStarted();
    }
}
