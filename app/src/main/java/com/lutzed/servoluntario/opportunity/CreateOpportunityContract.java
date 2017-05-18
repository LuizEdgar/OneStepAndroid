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

        void swapCauses(List<? extends SelectableItem> causes);

        void swapSkills(List<? extends SelectableItem> skills);

        void setOpportunity(Opportunity opportunity);

        void showCreateNewContact();

        void showAddNewCause(List<Long> checkedItems);

        void showAddNewSkill(List<Long> checkedItems);

        void setTitle(String title);

        void setDescription(String description);

        void setVolunteersNumber(Integer volunteersNumber);

        void setTimeCommitment(String timeCommitment);

        void setOthersRequirements(String othersRequirements);

        void setTags(String tags);
    }

    interface Presenter extends BasePresenter {
        void attemptCreateOpportunity(String title, String description, Contact contact, List<Long> skillIds, List<Long> causeIds, String volunteersNumber, String timeCommitment, String othersRequirements, String tags);

        void loadContacts();

        void createNewContact();

        void addNewCause();

        void addNewSkill();

        void onNewSelectableItemsAdded(List<? extends SelectableItem> parcelableArrayListExtra);
    }
}
