package com.lutzed.servoluntario.volunteer;

import com.lutzed.servoluntario.interfaces.BasePresenter;
import com.lutzed.servoluntario.interfaces.BaseView;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.Image;
import com.lutzed.servoluntario.models.SelectableItem;

import java.util.List;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public interface VolunteerContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void setContacts(List<Contact> contacts);

        void addCauses(List<? extends SelectableItem> causes);

        void addSkills(List<? extends SelectableItem> skills);

        void setName(String title);

        void setAbout(String description);

        void setLocation(String location);

        void addImages(List<Image> images);

        void setCoverImage(String url);

        void showEditVolunteer();

        void signOut();

        void setOccupation(String occupation);

        void setBirthAt(String format);

        void setGender(String gender);
    }

    interface Presenter extends BasePresenter {
        void loadOpportunity();

        void onEditVolunteerClicked();

        void signOut();
    }
}
