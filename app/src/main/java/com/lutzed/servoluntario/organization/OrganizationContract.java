package com.lutzed.servoluntario.organization;

import com.lutzed.servoluntario.interfaces.BasePresenter;
import com.lutzed.servoluntario.interfaces.BaseView;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.Image;
import com.lutzed.servoluntario.models.Organization;
import com.lutzed.servoluntario.models.SelectableItem;

import java.util.List;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public interface OrganizationContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void setContacts(List<Contact> contacts);

        void setCauses(List<? extends SelectableItem> causes);

        void setSkills(List<? extends SelectableItem> skills);

        void setName(String title);

        void setAbout(String description);

        void setCnpj(String cnpj);

        void setSite(String site);

        void setMission(String mission);

        void setLocation(String location);

        void setEstablishedAt(String time);

        void setImages(List<Image> images);

        void setCoverImage(String url);

        void showEditOrganization();

        void signOut();

        void setSize(Integer size);
    }

    interface Presenter extends BasePresenter {
        void updateOrganization(Organization organization);

        void loadOrganization();

        void onEditOrganizationClicked();

        void signOut();
    }
}
