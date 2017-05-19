package com.lutzed.servoluntario.opportunity;

import com.lutzed.servoluntario.interfaces.BasePresenter;
import com.lutzed.servoluntario.interfaces.BaseView;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.SelectableItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public interface OpportunityContract {

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
    }

    interface Presenter extends BasePresenter {
        void attemptCreateOpportunity(String title, String description, Contact contact, List<Long> skillIds, List<Long> causeIds, String volunteersNumber, String timeCommitment, String othersRequirements, String tags);

        void loadContacts();

        void createNewContact();

        void addNewCause(List<Long> existingCauseIds);

        void addNewSkill(List<Long> existingSkillIds);

        void onNewItemsSelection(ArrayList<SelectableItem> parcelableArrayListExtra, ArrayList<SelectableItem> parcelableArrayListExtra1);

        void addNewContact(String name, String phone, String email);
    }
}
