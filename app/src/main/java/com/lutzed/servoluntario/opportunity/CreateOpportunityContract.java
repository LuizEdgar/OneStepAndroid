package com.lutzed.servoluntario.opportunity;

import com.lutzed.servoluntario.interfaces.BasePresenter;
import com.lutzed.servoluntario.interfaces.BaseView;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.models.SelectableItem;

import java.util.List;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public interface CreateOpportunityContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void resetErrors();

        void setContacts(List<Contact> contacts, List<Long> selectedContactsIds);

        void addCauses(List<? extends SelectableItem> causes);

        void addSkills(List<? extends SelectableItem> skills);

        void setOpportunity(Opportunity opportunity);

        void showCreateNewContact();

        void showAddNewCause(List<Long> excludedCauses);

        void showAddNewSkill(List<Long> excludedCauses);
    }

    interface Presenter extends BasePresenter {
        void attemptCreateOpportunity();

        void loadContacts();

        void loadSkills();

        void loadCauses();

        void createNewContact();

        void addNewCause();

        void addNewSkill();

        void onNewSelectableItemsAdded(List<? extends SelectableItem> parcelableArrayListExtra);
    }
}
