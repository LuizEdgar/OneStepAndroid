package com.lutzed.servoluntario.opportunities;

import com.lutzed.servoluntario.interfaces.BasePresenter;
import com.lutzed.servoluntario.interfaces.BaseView;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.Image;
import com.lutzed.servoluntario.models.SelectableItem;

import java.util.List;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public interface OpportunityContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void setContact(Contact contact);

        void addCauses(List<? extends SelectableItem> causes);

        void addSkills(List<? extends SelectableItem> skills);

        void setTitle(String title);

        void setDescription(String description);

        void setVolunteersNumber(Integer volunteersNumber);

        void setTimeCommitment(String timeCommitment);

        void setOthersRequirements(String othersRequirements);

        void setTags(String tags);

        void setLocation(String location);

        void setTime(String time);

        void addImages(List<Image> images);

        void setCreator(String creator);

        void setLocationToVirtual();

        void setTimeToOngoing();
    }

    interface Presenter extends BasePresenter {
        void loadOpportunity();
    }
}
